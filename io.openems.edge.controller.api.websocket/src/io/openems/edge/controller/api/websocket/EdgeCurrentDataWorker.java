package io.openems.edge.controller.api.websocket;

import org.java_websocket.WebSocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import io.openems.common.session.Role;
import io.openems.common.types.ChannelAddress;
import io.openems.common.websocket.CurrentDataWorker;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.component.OpenemsComponent;

public class EdgeCurrentDataWorker extends CurrentDataWorker {

	/**
	 * The access level Role of this worker
	 */
	private final Role role;

	private final EdgeWebsocketHandler parent;

	public EdgeCurrentDataWorker(EdgeWebsocketHandler parent, WebSocket websocket, Role role) {
		super(websocket);
		this.parent = parent;
		this.role = role;
	}

	@Override
	protected JsonElement getChannelValue(ChannelAddress channelAddress) {
		for (OpenemsComponent component : this.parent.parent.components) {
			if (component.id().equals(channelAddress.getThingId())) {
				Channel<?> channel;
				try {
					channel = component.channel(channelAddress.getChannelId());
				} catch (IllegalArgumentException e) {
					return JsonNull.INSTANCE;
				}
				return channel.value().asJson();
			}
		}
		return JsonNull.INSTANCE;

		// Optional<Channel> channelOpt = thingRepository.getChannel(channelAddress);
		// if (channelOpt.isPresent()) {
		// Channel channel = channelOpt.get();
		// try {
		// channel.assertReadAllowed(this.role);
		// } catch (AccessDeniedException e) {
		// WebSocketUtils.sendNotificationOrLogError(this.websocket, new JsonObject(),
		// LogBehaviour.WRITE_TO_LOG,
		// Notification.CHANNEL_ACCESS_DENIED, channelAddress, this.role);
		// return Optional.empty();
		// }
		// try {
		// return
		// Optional.ofNullable(JsonUtils.getAsJsonElement(databus.getValue(channel).orElse(null)));
		// } catch (NotImplementedException e) {
		// WebSocketUtils.sendNotificationOrLogError(this.websocket, new JsonObject(),
		// LogBehaviour.WRITE_TO_LOG,
		// Notification.VALUE_CONVERSION_FAILED, channelAddress, e.getMessage());
		// return Optional.empty();
		// }
		// } else {
		// WebSocketUtils.sendNotificationOrLogError(this.websocket, new JsonObject(),
		// LogBehaviour.WRITE_TO_LOG,
		// Notification.CHANNEL_NOT_FOUND, channelAddress);
		// return Optional.empty();
		// }
	}
}

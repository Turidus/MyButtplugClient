package de.turidus.buttplugManager.events;

import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.List;

public record NewMessageListEvent(List<AbstractMessage> messageList) {}

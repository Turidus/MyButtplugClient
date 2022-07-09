package de.turidus.buttplugManager.events;

import de.turidus.buttplugClient.messages.AbstractMessage;

public record NewMessageEvent(int messageId, AbstractMessage message) {}

package de.turidus.buttplugClient.events;

import de.turidus.buttplugClient.messages.AbstractMessage;

public record SendMessageEvent(AbstractMessage message) {

}

package de.turidus.buttplugClient.events;

import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.List;

public record SendListOfMessagesEvent(List<AbstractMessage> messages) {


}

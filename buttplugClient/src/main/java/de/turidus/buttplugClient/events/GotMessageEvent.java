package de.turidus.buttplugClient.events;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugClient.messages.Message;

public class GotMessageEvent {

    public final Message     msg;
    public final MessageType messageType;

    public GotMessageEvent(Message msg, MessageType messageType) {

        this.msg = msg;
        this.messageType = messageType;
    }

}

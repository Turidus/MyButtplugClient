package de.turidus.buttplugClient.messages.statusMessages;

import de.turidus.buttplugClient.messages.AbstractMessage;

public class Ping extends AbstractMessage {

    private Ping() {}

    public Ping(int id) {
        super(id);
    }

}

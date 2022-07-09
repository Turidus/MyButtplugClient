package de.turidus.buttplugClient.messages.enumerationMessages;

import de.turidus.buttplugClient.messages.AbstractMessage;

public class RequestDeviceList extends AbstractMessage {

    private RequestDeviceList() {
    }

    public RequestDeviceList(int id) {
        super(id);
    }

}

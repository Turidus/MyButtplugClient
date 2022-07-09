package de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage;

import de.turidus.buttplugClient.messages.AbstractMessage;

public class StopAllDevices extends AbstractMessage {

    private StopAllDevices() {
    }

    public StopAllDevices(int id) {
        super(id);
    }

}

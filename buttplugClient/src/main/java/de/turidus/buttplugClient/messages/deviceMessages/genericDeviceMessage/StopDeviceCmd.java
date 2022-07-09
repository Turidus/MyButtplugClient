package de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

public class StopDeviceCmd extends AbstractDeviceMessage {

    private StopDeviceCmd() {
    }

    public StopDeviceCmd(int id, int deviceIndex) {
        super(id, deviceIndex);
    }

}

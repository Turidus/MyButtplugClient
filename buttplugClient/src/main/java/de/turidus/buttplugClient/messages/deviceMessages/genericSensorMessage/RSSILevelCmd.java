package de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

public class RSSILevelCmd extends AbstractDeviceMessage {

    public RSSILevelCmd() {
    }

    public RSSILevelCmd(int id, int deviceIndex) {
        super(id, deviceIndex);
    }

}

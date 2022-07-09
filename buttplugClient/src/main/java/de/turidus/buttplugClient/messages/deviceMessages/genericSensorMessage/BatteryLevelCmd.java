package de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

public class BatteryLevelCmd extends AbstractDeviceMessage {

    private BatteryLevelCmd() {
    }

    public BatteryLevelCmd(int id, int deviceIndex) {
        super(id, deviceIndex);
    }

}

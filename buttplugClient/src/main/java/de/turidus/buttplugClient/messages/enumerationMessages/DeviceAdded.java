package de.turidus.buttplugClient.messages.enumerationMessages;

import de.turidus.buttplugClient.devices.DeviceData;
import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.HashMap;
import java.util.Objects;

public class DeviceAdded extends AbstractMessage {

    public String                                             DeviceName;
    public int                                                DeviceIndex;
    public HashMap<String, DeviceData.DeviceMessageAttribute> DeviceMessages;

    private DeviceAdded() {
    }

    public DeviceAdded(int id, String deviceName, int deviceIndex, HashMap<String, DeviceData.DeviceMessageAttribute> deviceMessages) {
        super(id);
        this.DeviceName = deviceName;
        this.DeviceIndex = deviceIndex;
        this.DeviceMessages = deviceMessages;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        DeviceAdded that = (DeviceAdded) o;
        return DeviceIndex == that.DeviceIndex && Objects.equals(DeviceName, that.DeviceName) &&
               Objects.equals(DeviceMessages, that.DeviceMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), DeviceName, DeviceIndex, DeviceMessages);
    }

}

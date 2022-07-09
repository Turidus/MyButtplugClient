package de.turidus.buttplugClient.messages.enumerationMessages;

import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.Objects;

public class DeviceRemoved extends AbstractMessage {

    public int DeviceIndex;

    private DeviceRemoved() {
    }

    public DeviceRemoved(int id, int deviceIndex) {
        super(id);
        this.DeviceIndex = deviceIndex;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        DeviceRemoved that = (DeviceRemoved) o;
        return DeviceIndex == that.DeviceIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), DeviceIndex);
    }

}

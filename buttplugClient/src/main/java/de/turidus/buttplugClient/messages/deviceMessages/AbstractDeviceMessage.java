package de.turidus.buttplugClient.messages.deviceMessages;

import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.Objects;

abstract public class AbstractDeviceMessage extends AbstractMessage {

    public int DeviceIndex;

    public AbstractDeviceMessage() {
    }

    public AbstractDeviceMessage(int id, int deviceIndex) {
        super(id);
        this.DeviceIndex = deviceIndex;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        AbstractDeviceMessage that = (AbstractDeviceMessage) o;
        return DeviceIndex == that.DeviceIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), DeviceIndex);
    }

}

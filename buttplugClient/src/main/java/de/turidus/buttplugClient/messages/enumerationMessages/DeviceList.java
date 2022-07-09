package de.turidus.buttplugClient.messages.enumerationMessages;

import de.turidus.buttplugClient.devices.DeviceData;
import de.turidus.buttplugClient.messages.AbstractMessage;

import java.util.List;
import java.util.Objects;

public class DeviceList extends AbstractMessage {

    public List<DeviceData> Devices;

    private DeviceList() {
    }

    public DeviceList(int id, List<DeviceData> Devices) {
        super(id);
        this.Devices = Devices;
    }

    @Override
    public String toString() {
        return "DeviceList{" +
               "Devices=" + Devices +
               ", Id=" + Id +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        DeviceList that = (DeviceList) o;
        return Objects.equals(Devices, that.Devices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), Devices);
    }

}

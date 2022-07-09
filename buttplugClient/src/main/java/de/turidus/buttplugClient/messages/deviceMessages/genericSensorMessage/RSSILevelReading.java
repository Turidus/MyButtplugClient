package de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Objects;

public class RSSILevelReading extends AbstractDeviceMessage {

    public int RSSILevel;

    private RSSILevelReading() {
    }

    public RSSILevelReading(int id, int deviceIndex, int rssiLevel) {
        super(id, deviceIndex);
        this.RSSILevel = rssiLevel;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        RSSILevelReading that = (RSSILevelReading) o;
        return RSSILevel == that.RSSILevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), RSSILevel);
    }

}

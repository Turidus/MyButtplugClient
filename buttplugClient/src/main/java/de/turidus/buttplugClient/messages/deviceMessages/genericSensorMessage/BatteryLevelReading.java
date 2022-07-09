package de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage;

import de.turidus.buttplugClient.messages.deviceMessages.AbstractDeviceMessage;

import java.util.Objects;

public class BatteryLevelReading extends AbstractDeviceMessage {

    public double BatteryLevel;

    private BatteryLevelReading() {
    }

    public BatteryLevelReading(int id, int deviceIndex, double batteryLevel) {
        super(id, deviceIndex);
        this.BatteryLevel = batteryLevel;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        if(!super.equals(o)) {return false;}
        BatteryLevelReading that = (BatteryLevelReading) o;
        return Double.compare(that.BatteryLevel, BatteryLevel) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), BatteryLevel);
    }

}

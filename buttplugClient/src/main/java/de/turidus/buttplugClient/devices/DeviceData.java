package de.turidus.buttplugClient.devices;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class DeviceData {

    public String                                  DeviceName;
    public int                                     DeviceIndex;
    public HashMap<String, DeviceMessageAttribute> DeviceMessages;

    private DeviceData() {}

    public DeviceData(String deviceName, int deviceIndex, HashMap<String, DeviceMessageAttribute> deviceMessages) {
        this.DeviceName = deviceName;
        this.DeviceIndex = deviceIndex;
        this.DeviceMessages = deviceMessages;
    }

    @Override
    public String toString() {
        return "Device{" +
               "DeviceName='" + DeviceName + '\'' +
               ", DeviceIndex=" + DeviceIndex +
               ", DeviceMessages=" + DeviceMessages +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {return true;}
        if(o == null || getClass() != o.getClass()) {return false;}
        DeviceData deviceData = (DeviceData) o;
        return DeviceIndex == deviceData.DeviceIndex && DeviceName.equals(deviceData.DeviceName) && DeviceMessages.equals(deviceData.DeviceMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DeviceName, DeviceIndex, DeviceMessages);
    }

    public record DeviceMessageAttribute(int FeatureCount, int[] StepCount) {

        public DeviceMessageAttribute(int FeatureCount, int[] StepCount) {
            this.FeatureCount = FeatureCount;
            this.StepCount = StepCount;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {return true;}
            if(o == null || getClass() != o.getClass()) {return false;}
            DeviceMessageAttribute that = (DeviceMessageAttribute) o;
            return Objects.equals(FeatureCount, that.FeatureCount) && Arrays.equals(StepCount, that.StepCount);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(FeatureCount);
            result = 31 * result + Arrays.hashCode(StepCount);
            return result;
        }

        @Override
        public String toString() {
            return "DeviceMessageAttribute{" +
                   "FeatureCount=" + FeatureCount +
                   ", StepCount=" + Arrays.toString(StepCount) +
                   '}';
        }

        public int FeatureCount() {return FeatureCount;}

        public int[] StepCount() {return StepCount;}


    }
}

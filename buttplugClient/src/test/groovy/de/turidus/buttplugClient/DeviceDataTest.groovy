package de.turidus.buttplugClient

import de.turidus.buttplugClient.devices.DeviceData
import spock.lang.Specification

class DeviceDataTest extends Specification {

    def basic_device_test() {
        DeviceData.DeviceMessageAttribute deviceMsg = new DeviceData.DeviceMessageAttribute(2, new int[]{10, 20})
        HashMap<String, DeviceData.DeviceMessageAttribute> devicesMessages = new HashMap<>()
        devicesMessages.put("testCmd", deviceMsg)
        DeviceData device = new DeviceData("test", 1, devicesMessages)
        expect:
        device.DeviceName == "test"
        device.DeviceIndex == 1
        device.DeviceMessages.get("testCmd") == deviceMsg
        deviceMsg.FeatureCount() == 2
        deviceMsg.StepCount().length == 2
        deviceMsg.StepCount()[0] == 10
        deviceMsg.StepCount()[1] == 20
    }
}

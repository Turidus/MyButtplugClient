package de.turidus.buttplugManager.deviceManager

import de.turidus.buttplugClient.devices.DeviceData
import de.turidus.buttplugClient.events.SendListOfMessagesEvent
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelCmd
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceAdded
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceRemoved
import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.enums.MotorType
import de.turidus.buttplugManager.events.ClockEvent
import de.turidus.buttplugManager.events.DeviceAddedEvent
import de.turidus.buttplugManager.events.DeviceRemovedEvent
import de.turidus.buttplugManager.events.NotifyDeviceManagerEvent
import org.greenrobot.eventbus.EventBus
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class DeviceManagerTest extends Specification {

    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_vibrateCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_rotateCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_linearCmd
    @Shared
    DeviceData.DeviceMessageAttribute deviceMessageAttribute_stopCmd
    @Shared
    HashMap<String, DeviceData.DeviceMessageAttribute> devicesMessages
    @Shared
    DeviceData deviceData
    @Shared
    DeviceData deviceData2

    EventBus eventBus
    EventBusListener eventBusListener
    DeviceManager deviceManager
    AtomicInteger atomicInteger

    def setupSpec() {
        deviceMessageAttribute_vibrateCmd = new DeviceData.DeviceMessageAttribute(2, new int[]{10, 20})
        deviceMessageAttribute_rotateCmd = new DeviceData.DeviceMessageAttribute(1, new int[]{10})
        deviceMessageAttribute_linearCmd = new DeviceData.DeviceMessageAttribute(1, new int[]{2})
        deviceMessageAttribute_stopCmd = null
        devicesMessages = new HashMap<>()
        devicesMessages.put("VibrateCmd", deviceMessageAttribute_vibrateCmd)
        devicesMessages.put("RotateCmd", deviceMessageAttribute_rotateCmd)
        devicesMessages.put("LinearCmd", deviceMessageAttribute_rotateCmd)
        devicesMessages.put("StopDeviceCmd", deviceMessageAttribute_stopCmd)
        deviceData2 = new DeviceData("test", 0, devicesMessages)
        deviceData = new DeviceData("test", 1, devicesMessages)
    }

    def setup() {
        eventBus = new EventBus()
        eventBusListener = new EventBusListener(eventBus)
        deviceManager = new DeviceManager(eventBus, new AtomicInteger(), new HashMap<Integer, Device>())
        atomicInteger = new AtomicInteger()
    }

    def Device_Manager_Test() {
        expect:
        deviceManager != null
        deviceManager.mapOfDevices != null
    }

    def "Building a device"() {
        Device device = new Device(deviceData, atomicInteger)
        expect:
        device.name == deviceData.DeviceName
        device.deviceIndex == deviceData.DeviceIndex
        device.mapOfVibrationMotors.size() == deviceData.DeviceMessages.get("VibrateCmd").FeatureCount()
        device.mapOfRotationMotors.size() == deviceData.DeviceMessages.get("RotateCmd").FeatureCount()
        device.mapOfLinearMotors.size() == deviceData.DeviceMessages.get("LinearCmd").FeatureCount()
    }

    def "Adding a device to the device manager, this send a DeviceAddedEvent."() {
        expect:
        deviceManager.mapOfDevices.size() == 0
        when:
        deviceManager.addDevice(deviceData)
        then:
        deviceManager.mapOfDevices.size() == 1
        eventBusListener.classOfLastEvent == DeviceAddedEvent.class
    }

    def "Remove a device from the device manager, this send a DeviceRemovedEvent."() {
        deviceManager.addDevice(deviceData)
        when:
        deviceManager.removeDevice(deviceData.DeviceIndex)
        then:
        deviceManager.mapOfDevices.size() == 0
        eventBusListener.classOfLastEvent == DeviceRemovedEvent.class
    }

    def "Set all vibration motors on a devices to a target step"() {
        deviceManager.addDevice(deviceData)
        int deviceIndex = deviceManager.mapOfDevices.values()[0].deviceIndex
        Device device = deviceManager.mapOfDevices.get(deviceIndex)
        when:
        deviceManager.setTargetStepOnDeviceByType(deviceIndex, MotorType.VIBRATION, 0.5d)
        device.advanceAllMotors(100)
        then:
        device.mapOfVibrationMotors.get(0).getCurrentStep() == 0.5d
        device.mapOfVibrationMotors.get(1).getCurrentStep() == 0.5d
    }

    def "Stop all motors on all devices"() {
        deviceManager.addDevice(deviceData)
        deviceManager.addDevice(deviceData2)
        Device device = deviceManager.mapOfDevices.get(0)
        Device device2 = deviceManager.mapOfDevices.get(1)
        when:
        deviceManager.setTargetStepOnDeviceByType(0, MotorType.VIBRATION, 0.5d)
        deviceManager.setTargetStepOnDeviceByType(1, MotorType.ROTATION, 0.4d)
        deviceManager.advanceAllMotorsOnAllDevices(100)
        deviceManager.stopAllDevices()
        then:
        device.mapOfVibrationMotors.get(0).getCurrentStep() == 0
        device.mapOfVibrationMotors.get(1).getCurrentStep() == 0
        device2.mapOfRotationMotors.get(0).getCurrentStep() == 0
    }

    def "When empty, a clock tick event does not advance all devices and send all messages"() {
        when:
        deviceManager.onClockEvent(new ClockEvent(100l))
        then:
        eventBusListener.timesCalled == 0
    }

    def "When adding the same device twice, the second try is rejected"(){
        when:
        deviceManager.addDevice(deviceData)
        deviceManager.addDevice(deviceData)
        then:
        deviceManager.mapOfDevices.size() == 1
        deviceManager.mapOfDevices.values().size() == 1
        eventBusListener.timesCalled == 1
    }

    def "When not empty, a clock tick event advances all devices and sends all messages"() {
        when:
        deviceManager.addDevice(deviceData)
        deviceManager.onClockEvent(new ClockEvent(100l))
        then:
        eventBusListener.timesCalled == 2
        eventBusListener.getClassOfLastEvent() == SendListOfMessagesEvent.class
    }

    def "When receiving a NotifyDeviceManagerEvent containing a BatteryLevelCmd, the DeviceManager turns off battery level sensing for the device."(){
        deviceManager.addDevice(deviceData)
        Device device = deviceManager.mapOfDevices.get(1)
        BatteryLevelCmd blc = new BatteryLevelCmd(1, 1)
        when:
        deviceManager.onNotifyDeviceManagerEvent(new NotifyDeviceManagerEvent(blc))
        then:
        !device.senseBattery
    }

    def "When receiving a NotifyDeviceManagerEvent containing a RSSILevelCmd, the DeviceManager turns off RSSI level sensing for the device."(){
        deviceManager.addDevice(deviceData)
        Device device = deviceManager.mapOfDevices.get(1)
        RSSILevelCmd rlc = new RSSILevelCmd(1, 1)
        when:
        deviceManager.onNotifyDeviceManagerEvent(new NotifyDeviceManagerEvent(rlc))
        then:
        !device.senseRSSI
    }
}

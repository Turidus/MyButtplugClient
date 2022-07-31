package de.turidus.buttplugManager

import de.turidus.buttplugClient.devices.DeviceData
import de.turidus.buttplugClient.enums.ErrorCodeEnum
import de.turidus.buttplugClient.enums.MessageType
import de.turidus.buttplugClient.events.GotMessageEvent
import de.turidus.buttplugClient.events.SendListOfMessagesEvent
import de.turidus.buttplugClient.events.SendMessageEvent
import de.turidus.buttplugClient.messages.AbstractMessage
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelReading
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelReading
import de.turidus.buttplugClient.messages.deviceMessages.rawDeviceMessages.RawReading
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceAdded
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceList
import de.turidus.buttplugClient.messages.enumerationMessages.DeviceRemoved
import de.turidus.buttplugClient.messages.handshakeMessages.ServerInfo
import de.turidus.buttplugClient.messages.statusMessages.Error
import de.turidus.buttplugClient.messages.statusMessages.Ok
import de.turidus.buttplugClient.messages.statusMessages.Ping
import de.turidus.buttplugManager.deviceManager.Device
import de.turidus.buttplugManager.deviceManager.DeviceManager
import de.turidus.buttplugManager.events.ClockEvent
import de.turidus.buttplugManager.events.NewMessageEvent
import de.turidus.buttplugManager.events.NewMessageListEvent
import de.turidus.buttplugManager.events.NewRawReading
import de.turidus.buttplugManager.events.NotifyDeviceManagerEvent
import de.turidus.buttplugManager.utils.PingManager
import org.greenrobot.eventbus.EventBus
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class ManagerTest extends Specification {


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
    Manager manager

    def setupSpec() {
        deviceMessageAttribute_vibrateCmd = new DeviceData.DeviceMessageAttribute(2, new int[]{10, 20})
        deviceMessageAttribute_rotateCmd = new DeviceData.DeviceMessageAttribute(1, new int[]{10})
        deviceMessageAttribute_linearCmd = new DeviceData.DeviceMessageAttribute(1, new int[]{2})
        deviceMessageAttribute_stopCmd = null
        devicesMessages = new HashMap<>()
        devicesMessages.put("VibrateCmd", deviceMessageAttribute_vibrateCmd)
        devicesMessages.put("RotateCmd", deviceMessageAttribute_rotateCmd)
        devicesMessages.put("StopDeviceCmd", deviceMessageAttribute_stopCmd)
        deviceData2 = new DeviceData("test", 0, devicesMessages)
        deviceData = new DeviceData("test", 1, devicesMessages)
    }

    def setup() {
        eventBus = new EventBus()
        eventBusListener = new EventBusListener(eventBus)
        deviceManager = new DeviceManager(eventBus, new AtomicInteger(), new HashMap<Integer, Device>())
        manager = new Manager(eventBus, deviceManager, new PingManager(eventBus, new AtomicInteger()))
    }

    def managerTest() {
        expect:
        manager != null
    }

    def "A ServerInfo is correctly received and sends an appropriate ping"() {
        when:
        eventBus.post(new GotMessageEvent(new ServerInfo(0, "test", 2, 1000), MessageType.SERVER_INFO))
        eventBus.post(new ClockEvent(1000))
        then:
        eventBusListener.timesCalled == 5
        SendMessageEvent sendMessageEvent = (SendMessageEvent) eventBusListener.listOfReceivedEvents.get(0)
        sendMessageEvent.message().getClass() == Ping.class
    }

    def "A Ok answer for ping is received"() {
        when:
        eventBus.post(new GotMessageEvent(new ServerInfo(0, "test", 2, 1000), MessageType.SERVER_INFO))
        eventBus.post(new ClockEvent(1000))
        then:
        manager.sizeOfMessageMap() == 1
        when:
        eventBus.post(new GotMessageEvent(new Ok(0), MessageType.OK))
        then:
        eventBusListener.listOfReceivedEvents.get(0).getClass() == GotMessageEvent.class
        manager.sizeOfMessageMap() == 0
    }

    def "A Error answer for ping is received"() {
        when:
        eventBus.post(new GotMessageEvent(new ServerInfo(0, "test", 2, 1000), MessageType.SERVER_INFO))
        eventBus.post(new ClockEvent(1000))
        then:
        manager.sizeOfMessageMap() == 1
        when:
        eventBus.post(new GotMessageEvent(new Error(0, "Test", ErrorCodeEnum.ERROR_UNKNOWN), MessageType.ERROR))
        then:
        eventBusListener.listOfReceivedEvents.get(0).getClass() == GotMessageEvent.class
        manager.sizeOfMessageMap() == 0
    }

    def "If an device Error is received, a NotifyDeviceManagerEvent is fired, containing the failed Message."(){
        NewMessageEvent nme = new NewMessageEvent(1, new BatteryLevelCmd(1, 1))
        manager.onNewMessageEvent(nme)
        when:
        manager.onGotMessageEvent(new GotMessageEvent(new Error(1, "test", ErrorCodeEnum.ERROR_DEVICE), MessageType.ERROR))
        then:
        eventBusListener.timesCalled == 2
        eventBusListener.getClassOfLastEvent() == NotifyDeviceManagerEvent.class
        ((BatteryLevelCmd) ((NotifyDeviceManagerEvent) eventBusListener.listOfReceivedEvents.get(0)).failedMsg()) == nme.message()
    }

    def "A DeviceList is received and devices are made"() {
        List<DeviceData> deviceDataList = new ArrayList<>()
        deviceDataList.add(deviceData)
        deviceDataList.add(deviceData2)
        when:
        eventBus.post(new GotMessageEvent(new DeviceList(0, deviceDataList), MessageType.DEVICE_LIST))
        then:
        deviceManager.mapOfDevices.keySet().size() == 2
    }

    def "A DeviceAdded is received and a device is made, then removed"() {
        when:
        eventBus.post(new GotMessageEvent(new DeviceAdded(0, "test", 0, devicesMessages), MessageType.DEVICE_ADDED))
        then:
        deviceManager.mapOfDevices.keySet().size() == 1
        when:
        eventBus.post(new GotMessageEvent(new DeviceRemoved(0, 0), MessageType.DEVICE_REMOVED))
        then:
        deviceManager.mapOfDevices.keySet().size() == 0
    }

    def "A raw reading is received"() {
        when:
        eventBus.post(new GotMessageEvent(new RawReading(0, 0, "rx", new byte[]{}), MessageType.RAW_READING))
        then:
        eventBusListener.listOfReceivedEvents.size() == 2
        eventBusListener.listOfReceivedEvents.get(0).getClass() == NewRawReading.class
    }

    def "A new BatteryLevel is received and applied to the device"() {
        deviceManager.addDevice(deviceData)
        when:
        eventBus.post(new GotMessageEvent(new BatteryLevelReading(0, deviceData.DeviceIndex, 0.5), MessageType.BATTERY_LEVEL_READING))
        then:
        deviceManager.mapOfDevices.get(deviceData.DeviceIndex).batteryLevel == 0.5d
    }

    def "A new RSSILevel is received and applied to the device"() {
        deviceManager.addDevice(deviceData)
        when:
        eventBus.post(new GotMessageEvent(new RSSILevelReading(0, deviceData.DeviceIndex, 5), MessageType.RSSI_LEVEL_READING))
        then:
        deviceManager.mapOfDevices.get(deviceData.DeviceIndex).rssiLevel == 5
    }

    def "On receiving a NewMessageListEvent, a SendListOfMessagesEvent is fired and messages are put into the msg map."(){
        when:
        manager.onNewMessageListEvent(new NewMessageListEvent(Collections.singletonList(new Ok(1))))
        then:
        manager.sizeOfMessageMap() == 1
        eventBusListener.timesCalled == 1
        eventBusListener.getClassOfLastEvent() == SendListOfMessagesEvent.class
    }
}

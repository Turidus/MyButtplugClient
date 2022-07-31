package de.turidus.buttplugManager.deviceManager

import de.turidus.buttplugClient.devices.DeviceData
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.BatteryLevelCmd
import de.turidus.buttplugClient.messages.deviceMessages.genericSensorMessage.RSSILevelCmd
import de.turidus.buttplugClient.messages.enumerationMessages.RequestDeviceList
import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.events.ClockEvent
import de.turidus.buttplugManager.events.ConnectedEvent
import de.turidus.buttplugManager.events.NewMessageListEvent
import de.turidus.buttplugManager.events.SimpleMessageRequest
import org.greenrobot.eventbus.EventBus
import org.springframework.stereotype.Component
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

@Component
class DeviceDataRequestSupervisorTest extends Specification{

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
    long requestTimeInMS = 1000

    EventBus eventBus
    EventBusListener eventBusListener
    AtomicInteger atomicInteger
    DeviceManager deviceManager
    DeviceDataRequestSupervisor deviceRequestSupervisor

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
        deviceData = new DeviceData("test", 1, devicesMessages)
    }

    def setup() {
        eventBus = new EventBus()
        eventBusListener = new EventBusListener(eventBus)
        atomicInteger = new AtomicInteger()
        deviceManager = new DeviceManager(eventBus, atomicInteger, new HashMap<Integer, Device>())
        deviceRequestSupervisor = new DeviceDataRequestSupervisor(eventBus, requestTimeInMS, deviceManager, atomicInteger)
    }

    def "Build test"() {
        expect:
        deviceRequestSupervisor != null
    }

    def "If no connection event is received and enough time passes, no events are send."(){
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS / 2L as long))
        then:
        eventBusListener.timesCalled == 0
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS / 2L as long))
        then:
        eventBusListener.timesCalled == 0
    }

    def "If a connection event is received and enough time passes, list of messages is send, including one device request."(){
        when:
        deviceRequestSupervisor.onConnectedEvent(new ConnectedEvent())
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS / 2L as long))
        then:
        eventBusListener.timesCalled == 0
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS / 2L as long))
        then:
        eventBusListener.timesCalled == 1
        eventBusListener.getClassOfLastEvent() == NewMessageListEvent.class
        ((NewMessageListEvent)eventBusListener.listOfReceivedEvents.get(0)).messageList().
                find({msg -> msg.class == RequestDeviceList.class}) != null
    }

    def "If a device sense battery level is true, send battery level requests"(){
        deviceManager.addDevice(deviceData)
        Device device = deviceManager.mapOfDevices.get(1)
        device.senseBattery = false
        deviceRequestSupervisor.onConnectedEvent(new ConnectedEvent())
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS + 1))
        then:
        eventBusListener.timesCalled == 2
        ((NewMessageListEvent)eventBusListener.listOfReceivedEvents.get(0)).messageList()
                .stream()
                .filter(msg -> msg instanceof BatteryLevelCmd)
                .collect().isEmpty()
        when:
        device.senseBattery = true
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS))
        then:
        eventBusListener.timesCalled == 3
        !((NewMessageListEvent)eventBusListener.listOfReceivedEvents.get(0)).messageList()
                .stream()
                .filter(msg -> msg instanceof BatteryLevelCmd)
                .collect().isEmpty()
    }

    def "If a device sense RSSI level is true, send RSSI level requests"(){
        deviceManager.addDevice(deviceData)
        Device device = deviceManager.mapOfDevices.get(1)
        device.senseRSSI = false
        deviceRequestSupervisor.onConnectedEvent(new ConnectedEvent())
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS + 1))
        then:
        eventBusListener.timesCalled == 2
        ((NewMessageListEvent)eventBusListener.listOfReceivedEvents.get(0)).messageList()
                .stream()
                .filter(msg -> msg instanceof RSSILevelCmd)
                .collect().isEmpty()
        when:
        device.senseRSSI = true
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS))
        then:
        eventBusListener.timesCalled == 3
        !((NewMessageListEvent)eventBusListener.listOfReceivedEvents.get(0)).messageList()
                .stream()
                .filter(msg -> msg instanceof RSSILevelCmd)
                .collect().isEmpty()
    }

}

package de.turidus.buttplugManager.deviceManager

import de.turidus.buttplugClient.devices.DeviceData
import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.deviceManager.supervisors.DeviceGroupSupervisor
import org.greenrobot.eventbus.EventBus
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class DeviceGroupSupervisorTest extends Specification {

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
    DeviceGroupSupervisor deviceGroupSupervisor

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
        deviceGroupSupervisor = new DeviceGroupSupervisor(eventBus)
    }

    def builtTest() {
        expect:
        deviceGroupSupervisor != null
    }

    def "When a device is added by the deviceManager, the DeviceGroupSupervisor reacts to the DeviceAddedEvent."() {
        def dsgMock = Mock(DeviceGroupSupervisor)
        eventBus.register(dsgMock)
        when:
        deviceManager.addDevice(deviceData)
        then:
        1 * dsgMock.onDeviceAddedEvent(_)
        deviceGroupSupervisor.deviceGroupMap.size() == 3
    }

    def "When a device is removed by the deviceManager, the DeviceGroupSupervisor reacts to the DeviceRemovedEvent."() {
        def dsgMock = Mock(DeviceGroupSupervisor)
        eventBus.register(dsgMock)
        deviceManager.addDevice(deviceData)
        when:
        deviceManager.removeDevice(1)
        then:
        1 * dsgMock.onDeviceRemovedEvent(_)
        deviceGroupSupervisor.deviceGroupMap.values().parallelStream().anyMatch(dg -> dg.empty)
    }


}

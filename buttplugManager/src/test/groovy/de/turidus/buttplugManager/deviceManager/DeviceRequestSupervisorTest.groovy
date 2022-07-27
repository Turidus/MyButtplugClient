package de.turidus.buttplugManager.deviceManager

import de.turidus.buttplugClient.messages.enumerationMessages.RequestDeviceList
import de.turidus.buttplugClient.messages.handshakeMessages.RequestServerInfo
import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.events.ClockEvent
import de.turidus.buttplugManager.events.SimpleMessageRequest
import org.greenrobot.eventbus.EventBus
import org.springframework.stereotype.Component
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

@Component
class DeviceRequestSupervisorTest extends Specification{
    EventBus eventBus
    EventBusListener eventBusListener

    @Shared
    long requestTimeInMS = 1000

    def setup() {
        eventBus = new EventBus()
        eventBusListener = new EventBusListener(eventBus)
    }

    def "Build test"() {
        DeviceRequestSupervisor deviceRequestSupervisor = new DeviceRequestSupervisor(eventBus, requestTimeInMS)
        expect:
        deviceRequestSupervisor != null
    }

    def "After enough time passes, a request device list event is send."(){
        DeviceRequestSupervisor deviceRequestSupervisor = new DeviceRequestSupervisor(eventBus, requestTimeInMS)
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS / 2L as long))
        then:
        eventBusListener.timesCalled == 0
        when:
        deviceRequestSupervisor.onClockEvent(new ClockEvent(requestTimeInMS / 2L as long))
        then:
        eventBusListener.timesCalled == 1
        eventBusListener.getClassOfLastEvent() == SimpleMessageRequest.class
    }

}

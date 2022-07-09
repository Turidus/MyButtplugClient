package de.turidus.buttplugManager.utils

import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.events.ClockEvent
import org.greenrobot.eventbus.EventBus
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class PingManagerTest extends Specification {

    def built_PingManager() {
        PingManager pingManager = new PingManager(EventBus.getDefault(), new AtomicInteger())
        expect:
        pingManager != null
    }

    def "trigger ping manager"() {
        EventBusListener eventBusListener = new EventBusListener(EventBus.getDefault())
        PingManager pingManager = new PingManager(EventBus.getDefault(), new AtomicInteger())
        pingManager.setMaxPingTime(1000)
        when:
        pingManager.onClockEvent(new ClockEvent(1000))
        then:
        eventBusListener.timesCalled == 1
    }
}

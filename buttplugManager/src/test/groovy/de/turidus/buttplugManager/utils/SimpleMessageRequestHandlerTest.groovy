package de.turidus.buttplugManager.utils

import de.turidus.buttplugClient.enums.MessageType
import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.events.NewMessageEvent
import de.turidus.buttplugManager.events.SimpleMessageRequest
import org.greenrobot.eventbus.EventBus
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class SimpleMessageRequestHandlerTest extends Specification {

    EventBus eventBus
    EventBusListener eventBusListener
    SimpleMessageRequestHandler simpleMessageRequestHandler

    def setup() {
        eventBus = new EventBus()
        eventBusListener = new EventBusListener(eventBus)
        simpleMessageRequestHandler = new SimpleMessageRequestHandler(eventBus, new AtomicInteger())
    }

    def "A simple message request is handled"() {
        when:
        simpleMessageRequestHandler.onSimpleMessageRequest(new SimpleMessageRequest(MessageType.START_SCANNING))
        then:
        eventBusListener.getClassOfLastEvent() == NewMessageEvent.class
    }

    def "A not simple message is refused"() {
        when:
        simpleMessageRequestHandler.onSimpleMessageRequest(new SimpleMessageRequest(MessageType.REQUEST_SERVER_INFO))
        then:
        eventBusListener.timesCalled == 0
    }
}

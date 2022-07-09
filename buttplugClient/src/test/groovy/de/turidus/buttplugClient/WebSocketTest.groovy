package de.turidus.buttplugClient


import org.greenrobot.eventbus.EventBus
import spock.lang.Shared
import spock.lang.Specification

class WebSocketTest extends Specification {

    @Shared
    EventBus eventBus = EventBus.getDefault();

    @Shared
    EventCapture eventCapture = new EventCapture(EventBus.getDefault())

    def webSocket_test() {
        ButtplugClient webSocketWrapper = new ButtplugClient("127.0.0.1", 12345, eventBus)
        expect:
        webSocketWrapper.isOpen()
    }

}

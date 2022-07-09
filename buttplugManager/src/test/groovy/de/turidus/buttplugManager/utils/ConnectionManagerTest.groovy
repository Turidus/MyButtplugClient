package de.turidus.buttplugManager.utils

import de.turidus.buttplugClient.messages.handshakeMessages.ServerInfo
import de.turidus.buttplugManager.EventBusListener
import de.turidus.buttplugManager.events.ConnectToServerEvent
import de.turidus.buttplugManager.events.ConnectedEvent
import org.greenrobot.eventbus.EventBus
import spock.lang.Specification

class ConnectionManagerTest extends Specification {

    EventBus eventBus
    EventBusListener eventBusListener
    ConnectionManager connectionManager

    def setup() {
        eventBus = new EventBus()
        eventBusListener = new EventBusListener(eventBus)
        connectionManager = new ConnectionManager(eventBus)
    }

    def "Connection Manager is build"() {
        expect:
        connectionManager != null
    }

    def "On connectToServer event, a connection to a server is build and a ServerInfo is received"() {
        when:
        connectionManager.onConnectToServerEvent(new ConnectToServerEvent("127.0.0.1", 12345))
        then:
        connectionManager.isConnected()
        eventBusListener.timesCalled == 1
        eventBusListener.getClassOfLastEvent() == ConnectedEvent.class
    }
}

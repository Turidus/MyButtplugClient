package de.turidus.buttplugManager


import spock.lang.Specification

class SpringTest extends Specification {
    def configTest() {
        ManagerConfiguration managerConfiguration = new ManagerConfiguration("127.0.0.1", 12345)
        expect:
        managerConfiguration.getEventBus() != null
        managerConfiguration.getWebSocketWrapper() != null
    }
}

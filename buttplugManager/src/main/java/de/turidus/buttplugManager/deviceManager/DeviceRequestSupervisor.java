package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.ConnectedEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeviceRequestSupervisor {

    private final EventBus globalEventBus;
    private final double requestTimeInMS;
    private long deltaTInMS;
    private boolean connected;

    public DeviceRequestSupervisor(@Qualifier("managerEventBus") EventBus globalEventBus,
                                   @Value("${manager.deviceRequestTimeInMS}") double requestTimeInMS){
        this.globalEventBus = globalEventBus;
        this.requestTimeInMS = requestTimeInMS;
    }

    @Subscribe
    public void onClockEvent(ClockEvent clockEvent){
        if(!connected) return;
        deltaTInMS += clockEvent.deltaTInMS();
        if(deltaTInMS >= requestTimeInMS) {
            requestDeviceList();
            deltaTInMS -= requestTimeInMS;
        }
    }

    @Subscribe
    public void onConnectedEvent(ConnectedEvent event){
        connected = true;
    }

    private void requestDeviceList() {
        globalEventBus.post(new SimpleMessageRequest(MessageType.REQUEST_DEVICE_LIST));
    }

}

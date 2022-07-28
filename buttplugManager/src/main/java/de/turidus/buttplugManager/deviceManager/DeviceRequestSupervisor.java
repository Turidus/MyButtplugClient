package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.ConnectedEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeviceRequestSupervisor {

    private final EventBus eventBus;
    private final double   requestTimeInMS;
    private long deltaTInMS;
    private boolean connected = false;

    public DeviceRequestSupervisor(@Qualifier("managerEventBus") EventBus eventBus,
                                   @Value("${manager.deviceRequestTimeInMS}") double requestTimeInMS){
        this.eventBus = eventBus;
        this.requestTimeInMS = requestTimeInMS;
        this.eventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
        eventBus.post(new SimpleMessageRequest(MessageType.REQUEST_DEVICE_LIST));
    }

}

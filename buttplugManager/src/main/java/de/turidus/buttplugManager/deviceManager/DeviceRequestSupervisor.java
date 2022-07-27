package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.enums.MessageType;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Value;

public class DeviceRequestSupervisor {

    private final EventBus globalEventBus;
    private final double requestTimeInMS;
    private long deltaTInMS;

    public DeviceRequestSupervisor(EventBus globalEventBus,
                                   @Value("${manager.deviceRequestTimeInMS}") double requestTimeInMS){
        this.globalEventBus = globalEventBus;
        this.requestTimeInMS = requestTimeInMS;
    }

    @Subscribe
    public void onClockEvent(ClockEvent clockEvent){
        deltaTInMS += clockEvent.deltaTInMS();
        if(deltaTInMS >= requestTimeInMS) {
            requestDeviceList();
            deltaTInMS -= requestTimeInMS;
        }
    }

    private void requestDeviceList() {
        globalEventBus.post(new SimpleMessageRequest(MessageType.REQUEST_DEVICE_LIST));
    }

}

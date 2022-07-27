package de.turidus.buttplugManager.utils;

import de.turidus.buttplugClient.messages.statusMessages.Ping;
import de.turidus.buttplugManager.events.ClockEvent;
import de.turidus.buttplugManager.events.NewMessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PingManager {

    private final EventBus      eventBus;
    private final AtomicInteger idProvider;
    private       int           triggerTime   = Integer.MAX_VALUE;
    private       int           currentDeltaT = 0;

    public PingManager(@Qualifier("managerEventBus") EventBus eventBus, @Qualifier("idProvider") AtomicInteger idProvider) {
        this.eventBus = eventBus;
        this.idProvider = idProvider;
        this.eventBus.register(this);
    }

    public void setMaxPingTime(Integer maxPingTime) {
        triggerTime = maxPingTime > 2 ? maxPingTime / 2 : maxPingTime;
    }

    @Subscribe
    public void onClockEvent(ClockEvent clockEvent) {
        if(triggerTime == 0) {return;}
        currentDeltaT += clockEvent.deltaTInMS();
        if(currentDeltaT > triggerTime) {
            currentDeltaT -= triggerTime;
            int id = idProvider.getAndIncrement();
            eventBus.post(new NewMessageEvent(id, new Ping(id)));
        }
    }

}

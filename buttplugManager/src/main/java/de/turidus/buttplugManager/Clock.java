package de.turidus.buttplugManager;

import de.turidus.buttplugManager.events.ClockEvent;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Clock extends Thread {

    private final EventBus eventBus;
    private final int      loopTimeInMS;
    private       long     lastClockTime;
    private       boolean  running;

    public Clock(EventBus eventBus, @Value("${manager.loopTimeInMS}") int loopTimeInMS) {
        this.eventBus = eventBus;
        this.loopTimeInMS = loopTimeInMS;
        this.lastClockTime = System.currentTimeMillis();
        this.start();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while(running) {
            eventBus.post(new ClockEvent(System.currentTimeMillis() - lastClockTime));
            lastClockTime = System.currentTimeMillis();
            try {
                Thread.sleep(loopTimeInMS);
            } catch(InterruptedException ignore) {
            }
        }
    }

}

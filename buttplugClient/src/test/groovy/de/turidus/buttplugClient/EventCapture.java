package de.turidus.buttplugClient;

import de.turidus.buttplugClient.events.GotMessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class EventCapture {

    public final EventBus eventBus;

    public EventCapture(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void onGotMessageEvent(GotMessageEvent gotMessageEvent) {
        System.out.println(gotMessageEvent.messageType);
        System.out.println(gotMessageEvent.msg);
    }


}

package de.turidus.buttplugManager;

import de.turidus.buttplugClient.messages.handshakeMessages.ServerInfo;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class EventBusListener {

    public final List<Object> listOfReceivedEvents = new ArrayList<>();

    private final EventBus eventBus;
    public        int      timesCalled = 0;

    public EventBusListener(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onEvent(Object obj) {
        timesCalled++;
        listOfReceivedEvents.add(0, obj);
    }

    public Class<?> getClassOfLastEvent() {
        return listOfReceivedEvents.get(0).getClass();
    }

}

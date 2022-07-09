package de.turidus.buttplugManager.utils;

import de.turidus.buttplugClient.ButtplugClient;
import de.turidus.buttplugManager.events.ConnectToServerEvent;
import de.turidus.buttplugManager.events.ConnectedEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
public class ConnectionManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final EventBus eventBus;
    private ButtplugClient buttplugClient;

    public ConnectionManager(EventBus eventBus){
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onConnectToServerEvent(ConnectToServerEvent cse){
        try {
            buttplugClient = new ButtplugClient(cse.ip(), cse.port(), eventBus);
            eventBus.post(new ConnectedEvent());
        } catch(URISyntaxException | InterruptedException e) {
            logger.warn("Could not connect to the Intiface Server.", e);
        }
    }

    public boolean isConnected() {
        return buttplugClient != null && buttplugClient.isOpen();
    }

}

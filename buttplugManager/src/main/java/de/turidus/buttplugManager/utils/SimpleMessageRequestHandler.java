package de.turidus.buttplugManager.utils;

import de.turidus.buttplugClient.messages.AbstractMessage;
import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.StopAllDevices;
import de.turidus.buttplugClient.messages.enumerationMessages.RequestDeviceList;
import de.turidus.buttplugClient.messages.enumerationMessages.StartScanning;
import de.turidus.buttplugClient.messages.enumerationMessages.StopScanning;
import de.turidus.buttplugManager.events.NewMessageEvent;
import de.turidus.buttplugManager.events.SimpleMessageRequest;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SimpleMessageRequestHandler {

    private final Logger        logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final EventBus      eventBus;
    private final AtomicInteger idProvider;

    public SimpleMessageRequestHandler(EventBus eventBus, AtomicInteger idProvider) {

        this.eventBus = eventBus;
        this.idProvider = idProvider;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onSimpleMessageRequest(SimpleMessageRequest simpleMessageRequest) {
        switch(simpleMessageRequest.messageType()) {
            case START_SCANNING -> {
                int id = idProvider.getAndIncrement();
                postEvent(id, new StartScanning(id));
            }
            case STOP_SCANNING -> {
                int id = idProvider.getAndIncrement();
                postEvent(id, new StopScanning(id));
            }
            case REQUEST_DEVICE_LIST -> {
                int id = idProvider.getAndIncrement();
                postEvent(id, new RequestDeviceList(id));
            }
            case STOP_ALL_DEVICE -> {
                int id = idProvider.getAndIncrement();
                postEvent(id, new StopAllDevices(id));
            }
            default -> logWrongMessage(simpleMessageRequest);
        }
    }

    private void logWrongMessage(SimpleMessageRequest simpleMessageRequest) {
        logger.warn("Got a message that was not simple: " + simpleMessageRequest.messageType());
    }

    private void postEvent(int id, AbstractMessage abstractMessage) {
        eventBus.post(new NewMessageEvent(id, abstractMessage));
    }

}

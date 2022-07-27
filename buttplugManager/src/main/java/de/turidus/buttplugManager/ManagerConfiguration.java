package de.turidus.buttplugManager;

import org.greenrobot.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ManagerConfiguration {

    private final EventBus      eventBus = EventBus.getDefault();
    private final AtomicInteger idProvider;

    public ManagerConfiguration() {
        idProvider = new AtomicInteger(1);
    }

    @Bean("managerEventBus")
    public EventBus getEventBus() {
        return eventBus;
    }


    @Bean("idProvider")
    public AtomicInteger getIdProvider() {return idProvider;}

}

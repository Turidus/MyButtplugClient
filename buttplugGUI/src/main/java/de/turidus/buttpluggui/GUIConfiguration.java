package de.turidus.buttpluggui;

import org.greenrobot.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GUIConfiguration {

    private final EventBus eventBus = EventBus.getDefault();

    @Bean("guiEventBus")
    public EventBus getEventBus() {
        return eventBus;
    }

}

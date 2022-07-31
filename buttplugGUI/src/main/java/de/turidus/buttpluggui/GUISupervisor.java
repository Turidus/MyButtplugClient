package de.turidus.buttpluggui;

import javafx.animation.AnimationTimer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Qualifier;

public class GUISupervisor extends AnimationTimer {

    private final EventBus       eventBus;
    private       MainController mainController = null;

    public GUISupervisor(@Qualifier("guiEventBus") EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Override
    public void handle(long now) {
        if(mainController == null) {return;}
    }

    @Subscribe
    public void onMainControllerEvent(MainController mainController) {
        this.mainController = mainController;
    }

}

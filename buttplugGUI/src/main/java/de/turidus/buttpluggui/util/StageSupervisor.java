package de.turidus.buttpluggui.util;

import de.turidus.buttpluggui.ButtplugGUI;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageSupervisor extends AnimationTimer implements ApplicationListener<ButtplugGUI.StageReadyEvent> {


    private Stage stage = null;

    public StageSupervisor(){
        this.start();
    }

    @Override
    public void handle(long now) {
        if(stage == null) return;
    }



    @Override
    public void onApplicationEvent(ButtplugGUI.StageReadyEvent event) {
        stage = event.getStage();
    }

}

package de.turidus.buttpluggui.util;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static de.turidus.buttpluggui.ButtplugGUI.StageReadyEvent;
import static de.turidus.buttpluggui.util.FXMLHelper.loadFXML;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private static Scene scene;

    @SneakyThrows
    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        Stage stage = stageReadyEvent.getStage();
        scene = new Scene(loadFXML("de/turidus/buttpluggui/mainView"), 150, 500);
        stage.setScene(scene);
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
        stage.show();
    }

}

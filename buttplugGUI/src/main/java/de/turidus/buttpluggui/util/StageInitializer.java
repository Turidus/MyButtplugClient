package de.turidus.buttpluggui.util;

import de.turidus.buttpluggui.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static de.turidus.buttpluggui.ButtplugGUI.StageReadyEvent;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final EventBus guiEventBus;
    public MainController  mainController;

    public StageInitializer(@Qualifier("guiEventBus") EventBus guiEventBus){
        this.guiEventBus = guiEventBus;
    }


    @SneakyThrows
    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        Stage      stage  = stageReadyEvent.getStage();
        FXMLLoader loader = FXMLHelper.getFXMLLoader("de/turidus/buttpluggui/mainView");
        Scene scene = new Scene(loader.load(), 300, 432);
        mainController = loader.getController();
        mainController.setEventBus(guiEventBus);
        builtScene(stage, scene);
        stage.show();
    }

    private void builtScene(Stage stage, Scene scene) throws IOException {

        stage.setScene(scene);
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
    }

}

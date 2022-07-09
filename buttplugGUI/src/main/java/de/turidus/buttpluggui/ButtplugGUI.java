package de.turidus.buttpluggui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class ButtplugGUI extends Application {


    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(MainApplication.class).run();
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
        Platform.exit();
    }

    public static class StageReadyEvent extends ApplicationEvent {

        public StageReadyEvent(Stage stage) {super(stage);}

        public Stage getStage() {
            return (Stage) getSource();
        }

    }

}

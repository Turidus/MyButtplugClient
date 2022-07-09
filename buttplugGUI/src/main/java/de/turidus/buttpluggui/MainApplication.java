package de.turidus.buttpluggui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"de.turidus.buttpluggui", "de.turidus.buttplugManager"})
public class MainApplication {

    public static void main(String[] args) {
        Application.launch(ButtplugGUI.class, args);
    }

}
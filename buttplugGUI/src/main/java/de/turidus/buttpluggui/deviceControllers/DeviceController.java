package de.turidus.buttpluggui.deviceControllers;

import de.turidus.buttplugManager.deviceManager.Device;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class DeviceController {

    private final Logger     logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @FXML
    private       TitledPane devicePane;
    @FXML
    private       HBox       hBox;
    @FXML
    private       Rectangle  batteryLevel;
    @FXML
    private       Rectangle  rssiLevel;


    public void addDevice(Device device) {
        devicePane.setText(device.name);
    }

    public void addMotors(List<Parent> parentList) {
        Platform.runLater(() -> hBox.getChildren().addAll(parentList));
    }

    public void initialize() {}

}

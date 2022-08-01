package de.turidus.buttpluggui.deviceControllers;

import de.turidus.buttplugManager.deviceManager.Device;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class DeviceController {

    private final Logger     logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    public        Device     device;
    private       double     maxHeight;
    @FXML
    private       TitledPane devicePane;
    @FXML
    private       HBox       hBox;
    @FXML
    private       Rectangle  batteryLevel;
    @FXML
    private       Label      batLabel;
    @FXML
    private       Rectangle  rssiLevel;
    @FXML
    private       Label      rssiLabel;

    public void addDevice(Device device) {
        this.device = device;
        devicePane.setText(device.name);
    }

    public void addMotors(List<Parent> parentList) {
        Platform.runLater(() -> hBox.getChildren().addAll(parentList));
    }

    public void initialize() {
        maxHeight = batteryLevel.getHeight();
        changeLevelToEnabled(batteryLevel, Paint.valueOf("green"));
        changeLevelToEnabled(rssiLevel, Paint.valueOf("blue"));
    }

    public void setBatteryLevel(double batteryLevelValue) {
        Platform.runLater(() -> this.batteryLevel.setHeight(maxHeight * batteryLevelValue));
        System.out.println("Did run?");
    }

    public void setRSSILevel(int rssiLevelValue) {
        double mod = 1 + (rssiLevelValue / 100d);
        this.rssiLevel.setHeight(maxHeight * mod);
    }

    public void disableBatteryLevel(boolean disable) {
        if(disable) {
            changeLevelToDisabled(batteryLevel);
            batLabel.setText("dis");
        }
        else {
            changeLevelToEnabled(batteryLevel, Paint.valueOf("green"));
            batLabel.setText("bat");
        }
    }

    public void disableRSSILevel(boolean disable) {
        if(disable) {
            changeLevelToDisabled(rssiLevel);
            rssiLabel.setText("dis");
        }
        else {
            changeLevelToEnabled(rssiLevel, Paint.valueOf("blue"));
            rssiLabel.setText("RSSI");
        }
    }

    private void changeLevelToEnabled(Rectangle rectangle, Paint paint) {
        rectangle.setFill(paint);
    }

    private void changeLevelToDisabled(Rectangle rectangle) {
        rectangle.setFill(Paint.valueOf("red"));
    }

}

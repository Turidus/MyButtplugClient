package de.turidus.buttpluggui.deviceControllers;

import de.turidus.buttplugManager.deviceManager.Device;
import de.turidus.buttplugManager.deviceManager.VibrationMotor;
import de.turidus.buttpluggui.motorControllers.VibrationMotorController;
import de.turidus.buttpluggui.util.FXMLHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DeviceController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private Device                                 device;
    private Map<Integer, VibrationMotorController> vibrationMap = new HashMap<>();
    @FXML
    private TitledPane                             devicePane;
    @FXML
    private HBox                         hBox;


    public void addDevice(Device device) {
        this.device = device;
        devicePane.setText(device.name);
        addVibrationMotors(device.mapOfVibrationMotors);
    }

    private void addVibrationMotors(HashMap<Integer, VibrationMotor> mapOfVibrationMotors) {
        for(VibrationMotor vibrationMotor : mapOfVibrationMotors.values()){
            addVibrationMotor(vibrationMotor);
        }
    }

    private void addVibrationMotor(VibrationMotor vibrationMotor) {
        FXMLLoader loader = FXMLHelper.getFXMLLoader("de/turidus/buttpluggui/vibrationMotorView");
        try {
            Parent                   parent           = loader.load();
            VibrationMotorController vibrationController = loader.getController();
            vibrationMap.put(vibrationMotor.getMotorIndex(), vibrationController);
            vibrationController.addMotor(vibrationMotor);
            Platform.runLater(() -> hBox.getChildren().add(parent));
        } catch(IOException e) {
            logger.error("Device Controller could not be loaded.", e);
            throw new RuntimeException(e);
        }
    }

    public void stopAllMotors() {
        vibrationMap.values().forEach(vibrationMotorController -> vibrationMotorController.stopMotor());
    }

}

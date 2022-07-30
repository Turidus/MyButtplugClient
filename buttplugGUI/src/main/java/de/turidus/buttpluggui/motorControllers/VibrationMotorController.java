package de.turidus.buttpluggui.motorControllers;

import de.turidus.buttplugManager.deviceManager.VibrationMotor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.event.ChangeEvent;

public class VibrationMotorController {

    @FXML
    private Label motorLabel;

    @FXML
    private Slider mainSlider;

    @FXML
    private CheckBox manualCheck;

    @FXML
    private Button selectFunctionButton;

    @FXML
    private TextField groupTextField;
    private VibrationMotor motor;

    public void addMotor(VibrationMotor vibrationMotor) {
        motor = vibrationMotor;
        motorLabel.setText("Vibration " + vibrationMotor.getMotorIndex());
        mainSlider.setValue(motor.getSpeed().Speed());
        mainSlider.setMajorTickUnit(100d / motor.getStepCount());
        mainSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            motor.setNextTarget(newValue.doubleValue() / 100d);
        });
    }

    public void initialize(){
        builtSlider();
        manualCheck.selectedProperty().set(true);
    }

    private void builtSlider() {
        mainSlider.setMin(0);
        mainSlider.setMax(100);
        mainSlider.setShowTickLabels(true);
        mainSlider.setShowTickMarks(true);
        mainSlider.setSnapToTicks(true);
    }

    public void stopMotor() {
        mainSlider.setValue(0);
        motor.stop();
    }

}

package de.turidus.buttpluggui.motorControllers;

import de.turidus.buttplugManager.deviceManager.VibrationMotor;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    }

}

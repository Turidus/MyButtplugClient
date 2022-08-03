package de.turidus.buttpluggui.motorControllers;

import de.turidus.buttplugManager.deviceManager.motors.VibrationMotor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VibrationMotorController {

    private final Logger         logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    public        VibrationMotor motor;
    @FXML
    private       Label          motorLabel;
    @FXML
    private       Label          groupLeaderLabel;
    @FXML
    private       Slider         mainSlider;
    @FXML
    private       CheckBox       automatedCheckBox;
    @FXML
    private       Button         selectFunctionButton;
    @FXML
    private       TextField      groupTextField;

    public void addMotor(VibrationMotor vibrationMotor) {
        motor = vibrationMotor;
        motorLabel.setText("Vibration " + vibrationMotor.getMotorIndex());
        setupSlider();
        setupGroupTextField();
    }

    public void initialize() {
        builtSlider();
        builtManualCheck();
        builtGroupTextField();
    }

    public void stopMotor() {
        mainSlider.setValue(0);
        motor.stop();
    }

    public void automationSelected() {
        mainSlider.setDisable(automatedCheckBox.isSelected());
    }

    public void checkIfGroupLeader() {
        groupLeaderLabel.setVisible(motor.groupLeader);
        if(!automatedCheckBox.isSelected()) {mainSlider.setMouseTransparent(!motor.groupLeader);}
        automatedCheckBox.setDisable(!motor.groupLeader);
    }

    public void setSliderToValue() {
        if(!motor.groupLeader || automatedCheckBox.isSelected()) {
            mainSlider.setValue(100 * motor.getCurrentStep());
        }
    }


    private void setupGroupTextField() {
        groupTextField.setText(String.valueOf(motor.groupID));
    }

    private void setupSlider() {
        mainSlider.setValue(motor.getSpeed().Speed());
        mainSlider.setMajorTickUnit(100d / motor.getStepCount());
        mainSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if(motor.groupLeader && !automatedCheckBox.isSelected()) {motor.setNextTarget(newValue.doubleValue() / 100d);}
        });
    }

    private void builtGroupTextField() {
        groupTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                motor.groupID = Integer.parseInt(newValue);
                groupTextField.backgroundProperty().set(Background.fill(Paint.valueOf("white")));
                groupTextField.borderProperty().set(Border.stroke(Paint.valueOf("black")));
            } catch(NumberFormatException numberFormatException) {
                logger.warn("A group ID field received a non integer value.");
                groupTextField.backgroundProperty().set(Background.fill(Paint.valueOf("yellow")));
                groupTextField.borderProperty().set(Border.stroke(Paint.valueOf("red")));
            }
        });
        groupTextField.setTooltip(new Tooltip("An Integer value. Motors in the same group will be connected and controlled by the group leader."));
    }

    private void builtManualCheck() {
        automatedCheckBox.selectedProperty().set(false);
    }

    private void builtSlider() {
        mainSlider.setMin(0);
        mainSlider.setMax(100);
        mainSlider.setShowTickLabels(true);
        mainSlider.setShowTickMarks(true);
        mainSlider.setSnapToTicks(true);
    }

}

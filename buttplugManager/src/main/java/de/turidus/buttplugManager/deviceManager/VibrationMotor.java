package de.turidus.buttplugManager.deviceManager;

import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.VibrateCmd;

public class VibrationMotor extends Motor {


    public VibrationMotor(int motorIndex, int stepCount) {
        super(motorIndex, stepCount);
    }

    @Override
    public void calculateNextStep(double deltaTInMs) {
        if(manual) {currentStep = targetStep;}
        else {applySpeedToStep(deltaTInMs, speedFunction.apply(deltaTInMs, currentStep));}
        ensureValueRange();
    }

    public VibrateCmd.Speed getSpeed() {
        return new VibrateCmd.Speed(motorIndex, currentStep);
    }

}

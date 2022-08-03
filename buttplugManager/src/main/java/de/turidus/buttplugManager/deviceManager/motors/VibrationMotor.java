package de.turidus.buttplugManager.deviceManager.motors;

import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.VibrateCmd;

public class VibrationMotor extends Motor {


    public VibrationMotor(int motorIndex, int stepCount, int groupID) {
        super(motorIndex, stepCount, groupID);
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

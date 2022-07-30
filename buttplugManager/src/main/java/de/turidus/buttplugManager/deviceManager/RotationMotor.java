package de.turidus.buttplugManager.deviceManager;


import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.RotateCmd;

public class RotationMotor extends Motor {

    private boolean clockwise = true;

    public RotationMotor(int motorIndex, int stepCount, int groupID) {
        super(motorIndex, stepCount, groupID);
    }

    @Override
    public void calculateNextStep(double deltaTInMs) {
        if(manual) {currentStep = targetStep;}
        else {applySpeedToStep(deltaTInMs, speedFunction.apply(deltaTInMs, currentStep));}
        ensureValueRange();
    }

    public boolean isClockwise() {
        return clockwise;
    }

    /**
     * If the target step is greater than 0, the rotation will be clockwise, if smaller than zero, it will be counter-clockwise if supported.
     *
     * @param nextTargetStep
     *         the next target step in the range [-1;1].
     */
    @Override
    public void setNextTarget(double nextTargetStep) {
        if(nextTargetStep < -1 || nextTargetStep > 1) {
            throw new IllegalArgumentException("Target input was not in allowed range [-1;1], instead was: " + nextTargetStep);
        }
        super.setNextTarget(Math.abs(nextTargetStep));
        clockwise = nextTargetStep >= 0;
    }

    public RotateCmd.Rotation getRotation() {
        return new RotateCmd.Rotation(motorIndex, currentStep, clockwise);
    }

}

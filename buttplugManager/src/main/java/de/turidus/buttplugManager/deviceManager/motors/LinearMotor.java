package de.turidus.buttplugManager.deviceManager.motors;

import de.turidus.buttplugClient.messages.deviceMessages.genericDeviceMessage.LinearCmd;

public class LinearMotor extends Motor {


    private double currentSpeedInStepPerMs = 0;

    public LinearMotor(int motorIndex, int stepCount, int groupID) {
        super(motorIndex, stepCount, groupID);
    }

    @Override
    public void calculateNextStep(double deltaTInMs) {
        double speed = manual ? currentSpeedInStepPerMs : speedFunction.apply(deltaTInMs, currentStep);
        applySpeedToStep(deltaTInMs, speed);
        if(manual) {handelTargetStep(speed);}
        ensureValueRange();
    }

    public LinearCmd.Vector getVector() {
        double mod             = targetStep % stepSize;
        double reachableTarget = (targetStep - mod + (Math.round(mod / stepSize)) * stepSize);
        double duration        = Math.abs(reachableTarget - currentStep) / currentSpeedInStepPerMs;
        return new LinearCmd.Vector(motorIndex, (int) duration, currentStep);
    }

    public void setNextTargetAndDurationOfMovement(double targetStep, double durationInMS) {
        if(durationInMS <= 0) {throw new IllegalArgumentException("Duration input was not in allowed range (0-double.Max], was " + durationInMS);}
        setNextTarget(targetStep);
        currentSpeedInStepPerMs = (targetStep - currentStep) / durationInMS;
    }

    @Override
    public void stop() {
        synchronized(this) {
            currentSpeedInStepPerMs = 0;
            targetStep = 0;
            currentStep = 0;
            currentFineStep = 0;
        }
    }

    @Override
    protected void handelTargetStep(double speed) {
        if((speed >= 0 && currentStep >= targetStep) || (speed <= 0 && currentStep <= targetStep)) {
            double mod = targetStep % stepSize;
            currentStep = targetStep - mod + (Math.round(mod / stepSize)) * stepSize;
            currentFineStep = targetStep;
            currentSpeedInStepPerMs = 0;
        }
    }

}

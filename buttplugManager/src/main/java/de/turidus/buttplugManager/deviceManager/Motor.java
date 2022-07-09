package de.turidus.buttplugManager.deviceManager;

import java.util.function.BiFunction;

public abstract class Motor {

    protected final int                                motorIndex;
    protected final int                                stepCount;
    protected final double                             stepSize;
    protected       double                             currentStep     = 0;
    protected       double                             currentFineStep = 0;
    protected       double                             targetStep      = 0;
    protected       boolean                            manual          = true;
    protected       BiFunction<Double, Double, Double> speedFunction;

    protected Motor(int motorIndex, int stepCount) {
        this.motorIndex = motorIndex;
        this.stepCount = stepCount;
        stepSize = 1d / stepCount;
    }

    public abstract void calculateNextStep(double deltaTInMs);

    /**
     * @param nextTargetStep
     *         the next target step in the range [0;1].
     */
    public void setNextTarget(double nextTargetStep) {
        if(nextTargetStep < 0 || nextTargetStep > 1) {
            throw new IllegalArgumentException("Target input was not in allowed range [0;1], instead was: " + nextTargetStep);
        }
        targetStep = nextTargetStep;
    }

    public int getStepCount() {
        return stepCount;
    }

    public double getCurrentStep() {
        return currentStep;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public void setSpeedFunction(BiFunction<Double, Double, Double> speedFunction) {
        this.speedFunction = speedFunction;
    }

    protected void applySpeedToStep(double deltaTInMs, double speed) {
        currentFineStep += speed * deltaTInMs;
        currentStep = calculateNewStep();
    }

    protected void handelTargetStep(double speed) {
        if((speed >= 0 && currentStep >= targetStep) || (speed <= 0 && currentStep <= targetStep)) {
            double mod = targetStep % stepSize;
            currentStep = targetStep - mod + (Math.round(mod / stepSize)) * stepSize;
            currentFineStep = targetStep;
        }
    }

    protected double calculateNewStep() {
        double mod = currentFineStep % stepSize;
        return currentFineStep - mod + (Math.round(mod / stepSize)) * stepSize;
    }

    protected void ensureValueRange() {
        if(currentStep < 0) {currentStep = 0;}
        else if(currentStep > 1) {currentStep = 1;}
    }

    public void stop() {
        synchronized(this) {
            targetStep = 0;
            currentStep = 0;
            currentFineStep = 0;
        }
    }

}

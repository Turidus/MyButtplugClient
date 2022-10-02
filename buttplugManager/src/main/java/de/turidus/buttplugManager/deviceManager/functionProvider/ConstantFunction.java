package de.turidus.buttplugManager.deviceManager.functionProvider;

public class ConstantFunction extends AbstractFunction {

    private final double speedValue;

    public ConstantFunction(double speedValue, double maxRange) {
        super(maxRange);
        this.speedValue = speedValue;
    }

    @Override
    protected double calculateValue(double time) {
        return speedValue;
    }

}

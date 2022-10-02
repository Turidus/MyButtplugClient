package de.turidus.buttplugManager.deviceManager.functionProvider;

public class LinearFunction extends AbstractFunction {


    private final double startValue;
    private final double factor;

    protected LinearFunction(double startValue, double finalValue, double maxRange) {
        super(maxRange);
        this.startValue = startValue;
        factor = (finalValue - startValue) / maxRange;
    }

    @Override
    protected double calculateValue(double time) {
        return startValue + factor * time;
    }

}

package de.turidus.buttplugManager.deviceManager.functionProvider;

public abstract class AbstractFunction {

    public final double maxRange;

    protected AbstractFunction(double maxRange) {this.maxRange = maxRange;}

    public double apply(double time) {
        if(time < maxRange) {return Math.min(1, Math.max(0, calculateValue(time)));}
        return 0;
    }

    protected abstract double calculateValue(double time);

}

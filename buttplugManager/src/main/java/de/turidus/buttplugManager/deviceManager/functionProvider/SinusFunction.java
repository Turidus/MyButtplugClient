package de.turidus.buttplugManager.deviceManager.functionProvider;

public class SinusFunction extends AbstractFunction {

    private final double offsetY;
    private final double offsetX;
    private final double amplitude;
    private final double frequency;

    public SinusFunction(double offsetY, double offsetX, double amplitude, double frequency, double maxRange) {
        super(maxRange);
        this.offsetY = offsetY;
        this.offsetX = offsetX;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public SinusFunction(double maxRange) {
        super(maxRange);
        this.offsetX = 0;
        this.offsetY = 0.5;
        this.amplitude = 0.5;
        this.frequency = 1;
    }

    @Override
    protected double calculateValue(double time) {
        return amplitude * Math.sin(frequency * time + offsetX) + offsetY;
    }

}

package by.varyvoda.sigenc.domain.signal.modulation;

public class BaseModulation extends SignalModulation {

    private final double baseAmplitude;

    private final double baseFrequency;

    private final double basePhase;

    public BaseModulation(
        int sampleInterval,
        int bitNumber,
        double baseAmplitude,
        double baseFrequency,
        double basePhase
    ) {
        super(sampleInterval, bitNumber);
        this.baseAmplitude = baseAmplitude;
        this.baseFrequency = baseFrequency;
        this.basePhase = basePhase;
    }

    @Override
    public double amplitudeOf(int value) {
        return baseAmplitude * (value + 1);
    }

    @Override
    public double frequencyOf(int value) {
        return baseFrequency * (value + 1);
    }

    @Override
    public double phaseOf(int value) {
        return basePhase * (value + 1);
    }
}

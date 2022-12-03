package by.varyvoda.sigenc.domain.signal.modulation.base;

import by.varyvoda.sigenc.domain.signal.modulation.SignalModulator;
import lombok.Getter;

@Getter
public abstract class BaseModulator extends SignalModulator {

    private final double baseAmplitude;

    private final int baseFrequency;

    private final double basePhase;

    public BaseModulator(
        int frameSize,
        int bitNumber,
        double baseAmplitude,
        int baseFrequency,
        double basePhase
    ) {
        super(frameSize, bitNumber);
        this.baseAmplitude = baseAmplitude;
        this.baseFrequency = baseFrequency;
        this.basePhase = basePhase;
    }
}

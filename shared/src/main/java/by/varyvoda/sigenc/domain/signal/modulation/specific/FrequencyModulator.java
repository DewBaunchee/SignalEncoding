package by.varyvoda.sigenc.domain.signal.modulation.specific;

import by.varyvoda.sigenc.domain.signal.modulation.base.BaseModulator;
import by.varyvoda.sigenc.domain.signal.spectre.Spectre;

public class FrequencyModulator extends BaseModulator {

    public static final double EPSILON = 0.001;

    public FrequencyModulator(int frameSize, int bitNumber, double baseAmplitude, int baseFrequency, double basePhase) {
        super(frameSize, bitNumber, baseAmplitude, baseFrequency, basePhase);
    }

    @Override
    public double amplitudeOf(int value) {
        return getBaseAmplitude();
    }

    @Override
    public int frequencyOf(int value) {
        return getBaseFrequency() * (value + 1);
    }

    @Override
    public double phaseOf(int value) {
        return getBasePhase();
    }

    @Override
    public int valueOf(Spectre spectre) {
        for (int value = minValue; value <= maxValue; value++) {
            double amplitude = spectre.amplitude(frequencyOf(value));
            if (amplitude < EPSILON) return value;
        }
        return 0;
    }
}

package by.varyvoda.sigenc.domain.signal.modulation.specific;

import by.varyvoda.sigenc.domain.signal.modulation.base.BaseModulator;
import by.varyvoda.sigenc.domain.signal.spectre.Spectre;

import static java.lang.Math.round;

public class AmplitudeModulator extends BaseModulator {

    public AmplitudeModulator(int frameSize, int bitNumber, double baseAmplitude, int baseFrequency, double basePhase) {
        super(frameSize, bitNumber, baseAmplitude, baseFrequency, basePhase);
    }

    @Override
    public double amplitudeOf(int value) {
        return getBaseAmplitude() * (value + 1);
    }

    @Override
    public int frequencyOf(int value) {
        return getBaseFrequency();
    }

    @Override
    public double phaseOf(int value) {
        return getBasePhase();
    }

    @Override
    public int valueOf(Spectre spectre) {
        double amplitude = spectre.amplitude(getBaseFrequency());
        return (int) round(amplitude / getBaseAmplitude() - 1);
    }
}

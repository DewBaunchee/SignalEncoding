package by.varyvoda.sigenc.domain.signal.modulation.specific;

import by.varyvoda.sigenc.domain.signal.modulation.base.BaseModulator;
import by.varyvoda.sigenc.domain.signal.spectre.Spectre;

import static java.lang.Math.PI;
import static java.lang.Math.round;

public class PhaseModulator extends BaseModulator {

    private final double phaseStep = PI / invariantCount;

    public PhaseModulator(int frameSize, int bitNumber, double baseAmplitude, int baseFrequency, double basePhase) {
        super(frameSize, bitNumber, baseAmplitude, baseFrequency, basePhase);
    }

    @Override
    public double amplitudeOf(int value) {
        return getBaseAmplitude();
    }

    @Override
    public int frequencyOf(int value) {
        return getBaseFrequency();
    }

    @Override
    public double phaseOf(int value) {
        return getBasePhase() + phaseStep * value;
    }

    @Override
    public int valueOf(Spectre spectre) {
        double phase = spectre.phase(getBaseFrequency());
        return (int) round((phase - getBasePhase()) / phaseStep);
    }
}

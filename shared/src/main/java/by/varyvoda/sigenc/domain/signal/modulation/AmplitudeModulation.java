package by.varyvoda.sigenc.domain.signal.modulation;

public class AmplitudeModulation extends SignalModulation {

    public AmplitudeModulation(int sampleInterval, int bitNumber) {
        super(sampleInterval, bitNumber);
    }

    @Override
    public double amplitudeOf(int value) {
        return 0;
    }

    @Override
    public double frequencyOf(int value) {
        return 0;
    }

    @Override
    public double phaseOf(int value) {
        return 0;
    }
}

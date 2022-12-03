package by.varyvoda.sigenc.domain.signal.modulation.factory;

import by.varyvoda.sigenc.domain.signal.modulation.specific.AmplitudeModulator;
import by.varyvoda.sigenc.domain.signal.modulation.specific.FrequencyModulator;
import by.varyvoda.sigenc.domain.signal.modulation.specific.PhaseModulator;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
public class BaseModulatorFactory {

    private int frameSize = 1024;

    private int bitNumber = 1;

    private double baseAmplitude = 1;

    private int baseFrequency = 1;

    private double basePhase = 0;

    public static BaseModulatorFactory create() {
        return new BaseModulatorFactory();
    }

    public AmplitudeModulator amplitudeModulator() {
        return new AmplitudeModulator(frameSize, bitNumber, baseAmplitude, baseFrequency, basePhase);
    }

    public FrequencyModulator frequencyModulator() {
        return new FrequencyModulator(frameSize, bitNumber, baseAmplitude, baseFrequency, basePhase);
    }

    public PhaseModulator phaseModulator() {
        return new PhaseModulator(frameSize, bitNumber, baseAmplitude, baseFrequency, basePhase);
    }
}

package by.varyvoda.sigenc.domain.signal.modulation;

import by.varyvoda.sigenc.domain.bit.ByteBitStream;
import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.generator.FrequencyGenerator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class SignalModulation {

    protected final int sampleInterval;

    protected final int bitNumber;

    protected final FrequencyGenerator generator = new FrequencyGenerator();

    public Signal modulate(byte[] data) {
        try (ByteBitStream bitStream = new ByteBitStream(bitNumber, data)) {
            List<Signal.Datum> signalData = new ArrayList<>();
            bitStream.forEach((index, value) -> {
                for (int i = 0; i < sampleInterval; i++) {
                    signalData.add(
                        new Signal.Datum(
                            i + (index * sampleInterval),
                            generator.compute(
                                i,
                                sampleInterval,
                                amplitudeOf(value),
                                frequencyOf(value),
                                phaseOf(value)
                            )
                        )
                    );
                }
            });
            return new Signal(signalData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract double amplitudeOf(int value);

    public abstract double frequencyOf(int value);

    public abstract double phaseOf(int value);
}

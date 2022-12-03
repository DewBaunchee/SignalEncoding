package by.varyvoda.sigenc.domain.signal.modulation;

import by.varyvoda.sigenc.domain.algorithm.fft.FFT;
import by.varyvoda.sigenc.domain.algorithm.fourier.Fourier;
import by.varyvoda.sigenc.domain.bit.ByteInputBitStream;
import by.varyvoda.sigenc.domain.bit.ByteOutputBitStream;
import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.generator.FrequencyGenerator;
import by.varyvoda.sigenc.domain.signal.spectre.Spectre;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public abstract class SignalModulator {

    protected final int frameSize;

    protected final int bitNumber;

    protected final int maxValue;

    protected final int minValue;

    protected final int invariantCount;

    protected final FrequencyGenerator generator = new FrequencyGenerator();

    protected SignalModulator(int frameSize, int bitNumber) {
        this.frameSize = frameSize;
        this.bitNumber = bitNumber;
        this.maxValue = (int) pow(2.0, bitNumber) - 1;
        this.minValue = 0;
        this.invariantCount = maxValue - minValue + 1;
    }

    public Signal modulate(byte[] data) {
        try (ByteInputBitStream bitStream = new ByteInputBitStream(bitNumber, data)) {
            List<Signal.Datum> signalData = new ArrayList<>();
            bitStream.forEach((index, value) -> {
                for (int i = 0; i < frameSize; i++) {
                    signalData.add(
                        new Signal.Datum(
                            i + (index * frameSize),
                            generator.compute(
                                i,
                                frameSize,
                                amplitudeOf(value),
                                frequencyOf(value),
                                phaseOf(value)
                            )
                        )
                    );
                }
            });
            return new Signal(frameSize, signalData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract double amplitudeOf(int value);

    public abstract int frequencyOf(int value);

    public abstract double phaseOf(int value);

    public byte[] demodulate(Signal signal) {
        Fourier fourier = new FFT();
        List<Signal> frames = signal.frames();
        try (ByteOutputBitStream bitStream = new ByteOutputBitStream(bitNumber, new ByteArrayOutputStream())) {
            for (Signal frame : frames)
                bitStream.write(valueOf(fourier.spectre(frame)));
            bitStream.flush();
            return bitStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract int valueOf(Spectre spectre);
}

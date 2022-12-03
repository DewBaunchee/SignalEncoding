package by.varyvoda.sigenc.domain.signal;

import by.varyvoda.sigenc.domain.algorithm.complex.Complex;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

@RequiredArgsConstructor
public class Signal {

    private final int frameSize;

    private final List<Datum> data;

    public static Signal ofValues(int frameSize, int size, IntFunction<Datum> init) {
        List<Datum> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(init.apply(i));
        }
        return new Signal(frameSize, data);
    }

    public static Signal of(int frameSize, List<Datum> data) {
        return new Signal(frameSize, data);
    }

    public Signal concat(Signal signal) {
        List<Datum> data = new ArrayList<>(this.data.size() + signal.data.size());
        data.addAll(this.data);
        data.addAll(signal.data);
        return new Signal(frameSize, data);
    }

    public int frameSize() {
        return frameSize;
    }

    public List<Datum> data() {
        return data;
    }

    public int size() {
        return data.size();
    }

    public double value(int index) {
        return data.get(index).value;
    }

    public Complex[] toComplexArray() {
        Complex[] array = new Complex[data.size()];
        for (int i = 0; i < data.size(); i++) {
            array[i] = data.get(i).toComplex();
        }
        return array;
    }

    public int frameCount() {
        return size() / frameSize;
    }

    public Signal frame(int index) {
        int startIndex = frameSize * index;
        return Signal.of(frameSize, data.subList(startIndex, startIndex + frameSize));
    }

    public List<Signal> frames() {
        List<Signal> frames = new ArrayList<>();
        for (int i = 0; i < frameCount(); i++) {
            frames.add(frame(i));
        }
        return frames;
    }

    @lombok.Data
    @RequiredArgsConstructor
    public static class Datum {
        private final int i;
        private final double value;

        public Complex toComplex() {
            return new Complex(value, 0.0);
        }
    }
}

package by.varyvoda.sigenc.domain.signal;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

@RequiredArgsConstructor
public class Signal {

    private final List<Datum> data;

    public static Signal ofValues(int size, IntFunction<Datum> init) {
        List<Datum> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(init.apply(i));
        }
        return new Signal(data);
    }

    public Signal concat(Signal signal) {
        List<Datum> data = new ArrayList<>(this.data.size() + signal.data.size());
        data.addAll(this.data);
        data.addAll(signal.data);
        return new Signal(data);
    }

    public List<Datum> data() {
        return data;
    }

    public byte[] toByteArray() {
        return toByteArray(1);
    }

    public byte[] toByteArray(double qualifier) {
        byte[] array = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            array[i] = (byte) (qualifier * data.get(i).value);
        }
        return array;
    }

    @lombok.Data
    @RequiredArgsConstructor
    public static class Datum {
        private final int i;
        private final double value;
    }
}

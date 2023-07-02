package by.varyvoda.sigenc.domain.algorithm.smooth;

import by.varyvoda.sigenc.domain.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class SlippingSmooth implements SignalSmoothing {

    private final int m;

    public SlippingSmooth(int windowSize) {
        if (windowSize % 2 != 1) throw new IllegalArgumentException();
        this.m = (windowSize - 1) / 2;
    }

    @Override
    public String name() {
        return "Slipping";
    }

    @Override
    public Signal smooth(Signal signal) {
        List<Signal.Datum> smoothed = new ArrayList<>(signal.size());
        for (int i = 0; i < signal.size(); i++) {
            if (isInRange(i, signal.size())) {
                double sum = 0.0;
                int start = i - m;
                int end = i + m + 1;
                int length = end - start;
                for (int j = start; j < end; j++) {
                    sum += signal.value(j);
                }
                smoothed.add(new Signal.Datum(i, sum / length));
            } else {
                smoothed.add(signal.datum(i));
            }
        }
        return new Signal(signal.frameSize(), smoothed);
    }

    private boolean isInRange(int index, int length) {
        return (index - m) >= 0 && (index + m) < length;
    }
}

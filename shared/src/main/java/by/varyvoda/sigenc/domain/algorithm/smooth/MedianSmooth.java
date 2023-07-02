package by.varyvoda.sigenc.domain.algorithm.smooth;

import by.varyvoda.sigenc.domain.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class MedianSmooth implements SignalSmoothing {

    private final int k;
    private final int m;

    public MedianSmooth(int windowSize, int removingElements) {
        if (removingElements >= windowSize) throw new IllegalArgumentException();
        k = removingElements;
        m = (windowSize - 1) / 2 + k;
    }

    @Override
    public String name() {
        return "Median";
    }

    @Override
    public Signal smooth(Signal signal) {
        List<Signal.Datum> smoothed = new ArrayList<>(signal.size());
        for (int i = 0; i < signal.size(); i++) {
            if (isInRange(i, signal.size())) {
                double sum = 0.0;
                int start = i - m + k;
                int end = i + m + 1 - k;
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

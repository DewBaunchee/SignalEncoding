package by.varyvoda.sigenc.domain.algorithm.smooth;

import by.varyvoda.sigenc.domain.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class ParabolaSmooth implements SignalSmoothing {

    @Override
    public String name() {
        return "Parabola";
    }

    @Override
    public Signal smooth(Signal signal) {
        double qualifier = 1.0 / 2431;

        List<Signal.Datum> smoothed = new ArrayList<>(signal.size());
        for (int i = 0; i < signal.size(); i++) {
            if (isInRange(i, signal.size())) {
                smoothed.add(
                    new Signal.Datum(
                        i,
                        qualifier * (
                            110 * signal.value(i - 6)
                                - 198 * signal.value(i - 5)
                                - 135 * signal.value(i - 4)
                                + 110 * signal.value(i - 3)
                                + 390 * signal.value(i - 2)
                                + 600 * signal.value(i - 1)
                                + 677 * signal.value(i)
                                + 600 * signal.value(i + 1)
                                + 390 * signal.value(i + 2)
                                + 110 * signal.value(i + 3)
                                - 135 * signal.value(i + 4)
                                - 198 * signal.value(i + 5)
                                + 110 * signal.value(i + 6)
                            )
                    )
                );
            } else {
                smoothed.add(signal.datum(i));
            }
        }
        return new Signal(signal.frameSize(), smoothed);
    }

    private boolean isInRange(int index, int length) {
        int m = 6;
        return (index - m) >= 0 && (index + m) < length;
    }
}

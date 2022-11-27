package by.varyvoda.sigenc.domain.signal.generator;

import static java.lang.Math.sin;

public class SinusGenerator implements SignalGenerator {

    @Override
    public double compute(double a, double w, double t, double p) {
        return a * sin(w * t + p);
    }
}

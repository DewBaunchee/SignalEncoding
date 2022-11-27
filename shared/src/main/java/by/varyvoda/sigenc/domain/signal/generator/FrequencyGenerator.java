package by.varyvoda.sigenc.domain.signal.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.lang.Math.PI;

@Getter
@RequiredArgsConstructor
public class FrequencyGenerator extends SinusGenerator {

    public double compute(int index, int n, double a, double f, double phase) {
        return compute(a, 2 * PI, f * index / n, phase);
    }
}

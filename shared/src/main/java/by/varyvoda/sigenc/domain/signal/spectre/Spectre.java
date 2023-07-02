package by.varyvoda.sigenc.domain.signal.spectre;

import by.varyvoda.sigenc.domain.algorithm.complex.Complex;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;

@Data
@RequiredArgsConstructor
public class Spectre {

    private final int frameSize;
    private final List<Part> data;

    public Spectre(int frameSize, Complex[] complexes) {
        this.frameSize = frameSize;
        List<Part> data = new ArrayList<>(frameSize);
        for (int i = 0; i < complexes.length; i++) {
            data.add(new Part(frameSize, i, complexes[i]));
        }
        this.data = data;
    }

    public double amplitude(int f) {
        return data.get(f).a;
    }

    public double phase(int f) {
        return data.get(f).phase;
    }

    public List<Part> data() {
        return data;
    }

    public Spectre filter(Predicate<Part> predicate) {
        return new Spectre(
            frameSize,
            data.stream()
                .map(part -> predicate.test(part) ? part : new Part(part.f, 0.0, 0.0))
                .collect(Collectors.toList())
        );
    }

    public Spectre filter(List<Integer> frequencies) {
        return filter(part -> frequencies.contains(part.f));
    }

    @Data
    @RequiredArgsConstructor
    public static class Part {
        private final int f;
        private final double a;
        private final double phase;

        public Part(int n, int f, Complex complex) {
            this(f, 2 * complex.module() / n, -atan2(complex.i(), complex.r()) - PI / 2);
        }
    }
}

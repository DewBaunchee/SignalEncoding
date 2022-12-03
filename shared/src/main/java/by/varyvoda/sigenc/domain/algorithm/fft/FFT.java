package by.varyvoda.sigenc.domain.algorithm.fft;

import by.varyvoda.sigenc.domain.algorithm.complex.Complex;
import by.varyvoda.sigenc.domain.algorithm.fourier.Fourier;
import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.spectre.Spectre;

import static java.lang.Math.*;

public class FFT implements Fourier {

    @Override
    public Spectre spectre(Signal signal) {
        return new Spectre(signal.frameSize(), spectre(signal.toComplexArray()));
    }

    @Override
    public Spectre spectre(int minF, int maxF, Signal signal) {
        var spectre = spectre(signal.toComplexArray());
        var truncated = new Complex[maxF - minF];
        if (maxF - minF >= 0)
            System.arraycopy(spectre, minF, truncated, 0, maxF - minF);
        return new Spectre(signal.frameSize(), truncated);
    }

    private Complex[] spectre(Complex[] signal) {
        int n = signal.length;

        if (n == 1) return new Complex[]{signal[0]};

        if (n % 2 != 0)
            throw new IllegalArgumentException("Signal length is not a power of 2");

        int nHalf = n / 2;
        Complex[] signalPart = new Complex[nHalf];
        Complex[] even = signalPart(signalPart, signal, 0);
        Complex[] odd = signalPart(signalPart, signal, 1);

        Complex[] y = new Complex[n];
        for (int k = 0; k < nHalf; k++) {
            set(n, y, even, odd, k, 0);
            set(n, y, even, odd, k, nHalf);
        }
        return y;
    }

    private void set(int n, Complex[] y, Complex[] even, Complex[] odd, int indexBase, int indexShift) {
        double angle = -2 * (indexBase + indexShift) * PI / n;
        y[indexBase + indexShift] =
            Complex
                .of(cos(angle), sin(angle))
                .times(odd[indexBase])
                .plus(even[indexBase]);
    }

    private Complex[] signalPart(Complex[] container, Complex[] signal, int indexShift) {
        for (int k = 0; k < container.length; k++) container[k] = signal[2 * k + indexShift];
        return spectre(container);
    }
}

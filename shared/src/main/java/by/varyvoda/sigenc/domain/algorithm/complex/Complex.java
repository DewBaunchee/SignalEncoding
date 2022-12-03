package by.varyvoda.sigenc.domain.algorithm.complex;

import lombok.RequiredArgsConstructor;

import static java.lang.Math.*;

@RequiredArgsConstructor
public class Complex {

    private final double r;

    private final double i;

    public static Complex of(double r, double i) {
        return new Complex(r, i);
    }

    public double r() {
        return r;
    }

    public double i() {
        return i;
    }

    public Complex plus(Complex complex) {
        return new Complex(r + complex.r, i + complex.i);
    }

    public Complex plus(double value) {
        return new Complex(r + value, i);
    }

    public Complex minus(Complex complex) {
        return new Complex(r - complex.r, i - complex.i);
    }

    public Complex times(Complex complex) {
        return new Complex(r * complex.r - i * complex.i, r * complex.i + i * complex.r);
    }

    public Complex times(double value) {
        return new Complex(r * value, i * value);
    }

    public double module() {
        return sqrt(pow(r, 2) + pow(i, 2));
    }

    public String toString() {
        return r + " " + (i < 0 ? "-" : "+") + " " + abs(i) + "i";
    }
}
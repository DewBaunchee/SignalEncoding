package by.varyvoda.sigenc.domain.algorithm.smooth;

import by.varyvoda.sigenc.domain.signal.Signal;

public interface SignalSmoothing  {

    String name();

    Signal smooth(Signal signal);
}

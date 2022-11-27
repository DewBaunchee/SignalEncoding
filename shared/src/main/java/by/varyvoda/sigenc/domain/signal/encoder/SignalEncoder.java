package by.varyvoda.sigenc.domain.signal.encoder;

import by.varyvoda.sigenc.domain.signal.Signal;

public interface SignalEncoder {

    Signal encode(Signal carrier, Signal data);
}

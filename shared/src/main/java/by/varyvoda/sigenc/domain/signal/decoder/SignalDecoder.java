package by.varyvoda.sigenc.domain.signal.decoder;

import by.varyvoda.sigenc.domain.signal.Signal;

public class SignalDecoder {

    public Signal toSignal(int frameSize, byte[] bytes, int offset, int length, double divider) {
        return Signal.ofValues(frameSize, length, index -> new Signal.Datum(index + offset, bytes[index] / divider));
    }
}

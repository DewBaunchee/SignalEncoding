package by.varyvoda.sigenc.domain.signal.encoder;

import by.varyvoda.sigenc.domain.signal.Signal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SignalEncoder {

    private final int qualifier;

    public byte[] toBytes(Signal signal) {
        byte[] array = new byte[signal.data().size()];
        for (int i = 0; i < signal.size(); i++) {
            array[i] = (byte) (qualifier * signal.value(i));
        }
        return array;
    }
}

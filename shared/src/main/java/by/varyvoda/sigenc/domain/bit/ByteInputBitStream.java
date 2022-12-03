package by.varyvoda.sigenc.domain.bit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.IntConsumer;

public class ByteInputBitStream extends InputBitStream {

    private final int count;

    public ByteInputBitStream(int bitNumber, byte[] bytes) {
        super(bitNumber, new ByteArrayInputStream(bytes));
        if (bytes.length * 8 % bitNumber != 0)
            throw new IllegalArgumentException("Bytes length is not multiple of bit number");
        count = bytes.length * 8 / bitNumber;
    }

    public int count() {
        return count;
    }

    public void forEach(IntConsumer iterator) {
        forEach(((index, value) -> iterator.accept(value)));
    }

    public void forEach(IndexedIterator iterator) {
        for (int i = 0; i < count(); i++) {
            try {
                iterator.accept(i, read());
            } catch (IOException impossible) {
                throw new RuntimeException("Should never be thrown", impossible);
            }
        }
    }

    public interface IndexedIterator {
        void accept(int index, int value);
    }
}

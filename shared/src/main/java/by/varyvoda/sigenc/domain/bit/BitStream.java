package by.varyvoda.sigenc.domain.bit;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class BitStream extends InputStream {

    private final int bitNumber;

    private final InputStream inputStream;

    private byte buffer;

    private int byteOffset = -1;

    @Override
    public int read() throws IOException {
        int n = bitNumber;
        int result = 0;
        while (n-- > 0) result |= nextBit() << n;
        return result;
    }

    private int nextBit() throws IOException {
        if (byteOffset == -1) {
            byte[] bytes = inputStream.readNBytes(1);
            buffer = bytes.length == 0 ? 0 : bytes[0];
            byteOffset = Byte.SIZE - 1;
        }
        return ((buffer & 0xFF) >> byteOffset--) & 1;
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
        buffer = 0;
        byteOffset = 0;
    }
}

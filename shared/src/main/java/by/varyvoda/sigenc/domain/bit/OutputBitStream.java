package by.varyvoda.sigenc.domain.bit;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class OutputBitStream extends OutputStream {

    private final int bitNumber;

    private final OutputStream outputStream;

    private byte buffer = 0;

    private int offset = Byte.SIZE - 1;

    @Override
    public void write(int value) throws IOException {
        int n = bitNumber;
        while (n-- > 0) nextBit((value >> n) & 1);
    }

    private void nextBit(int bit) throws IOException {
        if (offset == -1) flush();
        buffer = (byte) (buffer | (bit << offset--));
    }

    @Override
    public void flush() throws IOException {
        outputStream.write(buffer);
        buffer = 0;
        offset = Byte.SIZE - 1;
    }
}

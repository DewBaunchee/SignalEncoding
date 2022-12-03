package by.varyvoda.sigenc.domain.bit;

import java.io.ByteArrayOutputStream;

public class ByteOutputBitStream extends OutputBitStream {

    private final ByteArrayOutputStream byteArrayOutputStream;

    public ByteOutputBitStream(int bitNumber, ByteArrayOutputStream byteArrayOutputStream) {
        super(bitNumber, byteArrayOutputStream);
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public byte[] toByteArray() {
        return byteArrayOutputStream.toByteArray();
    }
}

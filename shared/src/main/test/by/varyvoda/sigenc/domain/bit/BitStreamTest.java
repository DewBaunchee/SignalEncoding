package by.varyvoda.sigenc.domain.bit;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitStreamTest {

    @Test
    public void readTest() throws IOException {
        byte[] byteMinus127 = new byte[]{-127}; // 10000001
        testRead(1, byteMinus127, 1);
        testRead(2, byteMinus127, 2);
        testRead(8, byteMinus127, 129);


        byte[] byte127 = new byte[]{127}; // 01111111
        testRead(1, byte127, 0);
        testRead(2, byte127, 1);
        testRead(8, byte127, 127);


        byte[] byteMinus1 = new byte[]{-1}; // 11111111
        testRead(1, byteMinus1, 1);
        testRead(2, byteMinus1, 3);
        testRead(8, byteMinus1, 255);
    }

    private void testRead(int n, byte[] b, int expected) throws IOException {
        InputBitStream inputBitStream = new InputBitStream(n, new ByteArrayInputStream(b));
        assertEquals(expected, inputBitStream.read());
    }

    @Test
    public void writeTest() {
        byte[] byteMinus127 = new byte[]{-127}; // 10000001
        testWrite(1, byteMinus127);
        testWrite(2, byteMinus127);
        testWrite(8, byteMinus127);


        byte[] byte127 = new byte[]{127}; // 01111111
        testWrite(1, byte127);
        testWrite(2, byte127);
        testWrite(8, byte127);


        byte[] byteMinus1 = new byte[]{-1}; // 11111111
        testWrite(1, byteMinus1);
        testWrite(2, byteMinus1);
        testWrite(8, byteMinus1);
    }

    private void testWrite(int n, byte[] b) {
        try (ByteInputBitStream bibs = new ByteInputBitStream(n, b)) {
            ByteOutputBitStream bobs = new ByteOutputBitStream(n, new ByteArrayOutputStream());
            bibs.forEach(value -> {
                try {
                    bobs.write(value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            bobs.flush();
            assertArrayEquals(b, bobs.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

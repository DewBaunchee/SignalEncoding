package by.varyvoda.sigenc.domain.bit;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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


        byte[] byteMinus1 = new byte[]{-1}; // 01111111
        testRead(1, byteMinus1, 1);
        testRead(2, byteMinus1, 3);
        testRead(8, byteMinus1, 255);
    }

    private void testRead(int n, byte[] b, int expected) throws IOException {
        BitStream bitStream = new BitStream(n, new ByteArrayInputStream(b));
        assertEquals(expected, bitStream.read());
    }
}

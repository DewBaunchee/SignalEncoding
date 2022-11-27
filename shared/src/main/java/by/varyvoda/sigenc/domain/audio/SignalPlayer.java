package by.varyvoda.sigenc.domain.audio;

import by.varyvoda.sigenc.domain.signal.Signal;
import lombok.Getter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SignalPlayer {

    private final int sampleRate = 8000;

    private final AudioFormat audioFormat =
        new AudioFormat(sampleRate, 16, 1, true, false);

    @Getter
    private Signal signal;

    public void play() {
        try {
            SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat);
            line.open(audioFormat);
            line.start();

            byte[] bytes = signal.toByteArray();
            line.write(bytes, 0, bytes.length);
            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {

    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }
}

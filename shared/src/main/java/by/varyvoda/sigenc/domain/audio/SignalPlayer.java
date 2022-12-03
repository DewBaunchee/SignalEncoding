package by.varyvoda.sigenc.domain.audio;

import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.encoder.SignalEncoder;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SignalPlayer {

    private final AudioFormat audioFormat;

    private final Clip clip = AudioSystem.getClip();

    private final SignalEncoder signalEncoder = new SignalEncoder(127);

    public SignalPlayer(AudioFormat audioFormat) throws LineUnavailableException {
        this.audioFormat = audioFormat;
    }

    public void play(Signal signal, boolean repeat) {
        if (clip.isOpen())
            clip.close();
        try {
            byte[] bytes = signalEncoder.toBytes(signal);
            AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(bytes), audioFormat, bytes.length);
            clip.open(audioInputStream);
            if (repeat) clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        clip.close();
    }
}

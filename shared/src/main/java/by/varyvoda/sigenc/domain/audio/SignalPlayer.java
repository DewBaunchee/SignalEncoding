package by.varyvoda.sigenc.domain.audio;

import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.encoder.SignalEncoder;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SignalPlayer {

    private final AudioFormat audioFormat;

    private final Clip clip = AudioSystem.getClip();

    private final SignalEncoder signalEncoder;

    public SignalPlayer(AudioFormat audioFormat, SignalEncoder signalEncoder) throws LineUnavailableException {
        this.audioFormat = audioFormat;
        this.signalEncoder = signalEncoder;
    }

    public void play(Signal signal, boolean repeat) {
        if (clip.isOpen())
            clip.close();
        try {
            byte[] bytes = signalEncoder.toBytes(signal);
            AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(bytes), audioFormat, bytes.length);

            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("signal.wav"));
            audioInputStream.reset();

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

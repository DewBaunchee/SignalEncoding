package by.varyvoda.sigenc.domain.audio;

import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.decoder.SignalDecoder;
import lombok.RequiredArgsConstructor;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SignalListener {

    private final AudioFormat audioFormat;
    private final int delay;
    private final SignalDecoder signalDecoder = new SignalDecoder();
    private Thread thread;

    public void listen(Consumer<Signal> signalConsumer) throws LineUnavailableException {
        TargetDataLine microphone = AudioSystem.getTargetDataLine(audioFormat);
        microphone.open(audioFormat);
        microphone.start();
        thread = new Thread(() -> {
            int index = 0;
            byte[] buffer = new byte[(int) (audioFormat.getSampleRate() * delay / 1000)];

            int read;
            int remain = buffer.length;
            while (!thread.isInterrupted()) {
                read = microphone.read(buffer, 0, remain);
                if (read == 0) continue;

                remain -= read;
                if (remain != 0) continue;
                remain = buffer.length;

                signalConsumer.accept(
                    signalDecoder.toSignal(
                        audioFormat.getFrameSize(),
                        buffer,
                        index,
                        buffer.length,
                        1.0
                    )
                );
                index += buffer.length;
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}

package by.varyvoda.sigenc.domain.algorithm.fourier;

import by.varyvoda.sigenc.domain.signal.Signal;
import by.varyvoda.sigenc.domain.signal.spectre.Spectre;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

public interface Fourier {

     Spectre spectre(Signal signal);

     Spectre spectre(int minF, int maxF, Signal signal);

     default Signal signal(Spectre spectre, boolean addInitial, boolean withoutPhase) {
         int frameSize = spectre.getFrameSize();
         int minF = addInitial ? 1 : 0;
         int maxF = spectre.getData().size() / 2 - 2;

         List<Signal.Datum> data = new ArrayList<>(frameSize);
         for (int i = 0; i < frameSize; i++) {
             double sum = addInitial ? spectre.amplitude(0) / 2 : 0.0;

             for(int f = minF; f <= maxF; f++) {
                 sum += spectre.amplitude(f) * cos(2 * PI * i * f / frameSize - (withoutPhase ? 0.0 : spectre.phase(f)));
             }

             data.add(new Signal.Datum(i, sum));
         }

         return new Signal(frameSize, data);
     }
}

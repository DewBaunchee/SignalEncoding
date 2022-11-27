package by.varyvoda.sigenc.app

import by.varyvoda.sigenc.app.view.MainView
import javafx.application.Application
import tornadofx.*
import kotlin.math.sin

class SigencApp : App(MainView::class)

fun main(args: Array<String>) {
    Application.launch(SigencApp::class.java, *args)
}

private fun generateSineWavefreq(frequencyOfSignal: Int, seconds: Int): ByteArray? {
    // total samples = (duration in second) * (samples per second)
    val sampleRate = 8000
    val sin = ByteArray(seconds * sampleRate)
    val samplingInterval = (sampleRate / frequencyOfSignal).toDouble()
    for (i in sin.indices) {
        val angle = 2.0 * Math.PI * i / samplingInterval
        sin[i] = (sin(angle) * 127).toInt().toByte()
    }
    return sin
}

val dataLength = 16
val sampleRate = 8000
val time = dataLength / sampleRate
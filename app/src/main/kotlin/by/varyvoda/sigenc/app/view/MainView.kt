package by.varyvoda.sigenc.app.view

import by.varyvoda.sigenc.app.view.component.labeledSlider
import by.varyvoda.sigenc.app.view.util.allGrow
import by.varyvoda.sigenc.app.view.util.spacing
import by.varyvoda.sigenc.app.view.util.toSeries
import by.varyvoda.sigenc.domain.algorithm.fft.FFT
import by.varyvoda.sigenc.domain.audio.SignalListener
import by.varyvoda.sigenc.domain.audio.SignalPlayer
import by.varyvoda.sigenc.domain.signal.Signal
import by.varyvoda.sigenc.domain.signal.encoder.SignalEncoder
import by.varyvoda.sigenc.domain.signal.modulation.factory.BaseModulatorFactory
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.scene.layout.Priority
import tornadofx.*
import java.nio.ByteBuffer
import javax.sound.sampled.AudioFormat
import kotlin.math.roundToInt

typealias Chart = XYChart<Number, Number>

class MainView : View("Signal encoding") {

    override fun onDock() {
        super.onDock()
        primaryStage.isMaximized = true
    }

    private lateinit var chart: Chart
    private lateinit var received: Chart
    private lateinit var spectres: Chart

    private val modulatorFactory = BaseModulatorFactory.create()

    private val audioFormat = AudioFormat(48000f, 8, 1, true, false)
    private val player = SignalPlayer(audioFormat, SignalEncoder(127))
    private val listener = SignalListener(audioFormat, 100)

    private var value = 1
    private lateinit var signal: Signal
    private var initialized = false

    private var offset = 0
    private var frameIndex = 0

    override val root = vbox {
        hbox {
            spacing()

            button("repeat") {
                onAction = EventHandler {
                    player.play(signal, true)
                }
            }

            button("play") {
                onAction = EventHandler {
                    player.play(signal, false)
                }
            }

            button("stop") {
                onAction = EventHandler {
                    player.stop()
                }
            }

            labeledSlider(-1000.0, +1000.0, 1.0) {
                valueTransform = { "value = ${it.roundToInt()}" }
                onValue { value = it.roundToInt(); update() }
            }

            labeledSlider(0.0, 100.0, 1.0) {
                valueTransform = { "f = ${it.roundToInt()}" }
                onValue { modulatorFactory.setBaseFrequency(it.roundToInt()); update() }
            }

            allGrow()
        }

        hbox {
            spacing()

            var receivedSignal = Signal.empty(1024)
            button("Listen") {
                onAction = EventHandler {
                    val series = Series<Number, Number>()
                    listener.listen {
                        Platform.runLater {
                            series.data.addAll(it.toSeries("Received").data)
                        }
                        receivedSignal = receivedSignal.concat(it)
                    }
                    received.data.setAll(series)
                }
            }

            button("Replay") {
                onAction = EventHandler {
                    player.play(receivedSignal, false)
                }
            }

            button("Stop") {
                onAction = EventHandler {
                    listener.stop()
                }
            }

            button("Spectre") {
                onAction = EventHandler {
                    val fourier = FFT()
                    try {
                        val spectre = fourier.spectre(signal).filter(listOf(100))
                        spectres.data.setAll(fourier.signal(spectre, false, false).toSeries("Restored"))
                    } catch (ignored: Exception) {

                    }
                }
            }

            labeledSlider(0.0, 1024.0, 0.0) {
                valueTransform = { "offset = ${it.roundToInt()}" }
                onValue { offset = it.roundToInt() }
            }

            labeledSlider(0.0, 127.0, 1.0) {
                valueTransform = { "Frame index = ${it.roundToInt()}" }
                onValue { offset = it.roundToInt() }
            }
        }

        chart = linechart("Signal", NumberAxis(), NumberAxis()) {
            vgrow = Priority.ALWAYS
            animated = false
            createSymbols = false
            axisSortingPolicy = LineChart.SortingPolicy.NONE
        }

        received = linechart("Received", NumberAxis(), NumberAxis()) {
            vgrow = Priority.ALWAYS
            animated = false
            createSymbols = false
            axisSortingPolicy = LineChart.SortingPolicy.NONE
        }

        spectres = linechart("Received", NumberAxis(), NumberAxis()) {
            vgrow = Priority.ALWAYS
            animated = false
            createSymbols = false
            axisSortingPolicy = LineChart.SortingPolicy.NONE
        }

        initialized = true
        update()
    }

    private fun update() {
        if (!initialized) return

        val modulation = modulatorFactory.phaseModulator()

        val data = ByteBuffer.allocate(4).putInt(value).array()
        signal = modulation.modulate(data)
        val receivedValue = modulation.demodulate(signal)

        chart.data.setAll(signal.toSeries("Signal"))
        chart.title = "Decoded value: ${ByteBuffer.wrap(receivedValue).int}"
    }
}
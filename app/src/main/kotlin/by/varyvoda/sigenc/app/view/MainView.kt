package by.varyvoda.sigenc.app.view

import by.varyvoda.sigenc.app.view.component.labeledSlider
import by.varyvoda.sigenc.app.view.util.allGrow
import by.varyvoda.sigenc.app.view.util.spacing
import by.varyvoda.sigenc.app.view.util.toSeries
import by.varyvoda.sigenc.domain.audio.SignalListener
import by.varyvoda.sigenc.domain.audio.SignalPlayer
import by.varyvoda.sigenc.domain.signal.Signal
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

    private val modulatorFactory = BaseModulatorFactory.create()

    private val audioFormat = AudioFormat(8000f, 8, 1, true, false)
    private val player = SignalPlayer(audioFormat)
    private val listener = SignalListener(audioFormat, 100)

    private var value = 1
    private lateinit var signal: Signal
    private var initialized = false

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

            labeledSlider(-127.0, 127.0, 1.0) {
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
            button("listen") {
                onAction = EventHandler {
                    val series = Series<Number, Number>()
                    listener.listen {
                        Platform.runLater {
                            series.data.addAll(it.toSeries("asd").data)
                        }
                    }
                    received.data.add(series)
                }
            }

            button("stop") {
                onAction = EventHandler {
                    listener.stop()
                }
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

        initialized = true
        update()
    }

    private fun update() {
        if (!initialized) return

        val modulation = modulatorFactory.phaseModulator()

        val data = ByteBuffer.wrap(byteArrayOf(value.toByte())).array()
        signal = modulation.modulate(data)
        val receivedValue = modulation.demodulate(signal)

        chart.data.setAll(signal.toSeries("Signal"))
        chart.title = "Decoded value: ${receivedValue[0]}"
    }
}
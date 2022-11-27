package by.varyvoda.sigenc.app.view

import by.varyvoda.sigenc.app.view.component.labeledSlider
import by.varyvoda.sigenc.app.view.util.allGrow
import by.varyvoda.sigenc.app.view.util.spacing
import by.varyvoda.sigenc.app.view.util.toSeries
import by.varyvoda.sigenc.domain.audio.SignalPlayer
import by.varyvoda.sigenc.domain.signal.Signal
import by.varyvoda.sigenc.domain.signal.modulation.BaseModulation
import javafx.event.EventHandler
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.layout.Priority
import tornadofx.*
import kotlin.math.round
import kotlin.math.roundToInt

typealias Chart = XYChart<Number, Number>

class MainView : View("Signal encoding") {

    override fun onDock() {
        super.onDock()
        primaryStage.isMaximized = true
    }

    private var a = 1.0
    private var f = 1.0
    private var n = 64
    private var phase = 0.0

    private lateinit var chart: Chart

    private var initialized = false

    override val root = vbox {
        hbox {
            spacing()
            button("play") {
                onAction = EventHandler { SignalPlayer().let { it.signal = generateSignal(); it.play() } }

            }

            labeledSlider(1.0, 10.0, 1.0) {
                setNameTransform("a", 2)
                onValue { a = it; update() }
            }
            labeledSlider(1.0, 128.0, 1.0) {
                valueTransform = { "f = ${it.roundToInt()}" }
                onValue { f = round(it); update() }
            }
            labeledSlider(1.0, 2048.0, 512.0) {
                valueTransform = { "n = ${it.roundToInt()}" }
                onValue { n = it.roundToInt(); update() }
            }
            labeledSlider(0.0, 100.0, 0.0) {
                setNameTransform("phase", 2)
                onValue { phase = it; update() }
            }

            allGrow()
        }

        chart = linechart("Signal", NumberAxis(), NumberAxis()) {
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

        chart.data.setAll(generateSignal().toSeries("Signal"))
    }

    private fun generateSignal(): Signal {
        val sampleInterval = 1024;
        val modulation = BaseModulation(sampleInterval, 1, 1.0, 1.0, 0.0)
        val data = byteArrayOf(3)
        return modulation.modulate(data)
    }
}
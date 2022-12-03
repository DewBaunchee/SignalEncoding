package by.varyvoda.sigenc.app.view.util

import by.varyvoda.sigenc.domain.signal.Signal
import by.varyvoda.sigenc.domain.signal.spectre.Spectre
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ListChangeListener.Change
import javafx.collections.ObservableList
import javafx.scene.chart.XYChart.Data
import javafx.scene.chart.XYChart.Series
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*
import java.text.DecimalFormat

fun HBox.spacing(value: Double = 10.0) {
    paddingAll = value
    spacing = value
}

fun VBox.spacing(value: Double = 10.0) {
    paddingAll = value
    spacing = value
}

fun HBox.allGrow(priority: Priority = Priority.ALWAYS) {
    children.listenValue {
        it.list.forEach { child -> child.hgrow = priority }
    }
}

fun VBox.allGrow(priority: Priority = Priority.ALWAYS) {
    children.listenValue {
        it.list.forEach { child -> child.vgrow = priority }
    }
}

fun <T> ObservableValue<T>.listenValue(listener: ChangeListener<T>) {
    listener.changed(this, null, value)
    addListener(listener)
}

fun <T> ObservableList<T>.listenValue(listener: ListChangeListener<T>) {
    addListener(listener)
    listener.onChanged(object : Change<T>(this) {
        override fun next(): Boolean {
            return false
        }

        override fun reset() {
        }

        override fun getFrom(): Int {
            return -1
        }

        override fun getTo(): Int {
            return -1
        }

        override fun getRemoved(): MutableList<T> {
            return mutableListOf()
        }

        override fun getPermutation(): IntArray {
            return intArrayOf()
        }
    })
}

fun Signal.toSeries(name: String): Series<Number, Number> {
    return Series<Number, Number>().apply {
        setName(name)
        data.addAll(this@toSeries.data().map { Data(it.i, it.value) })
    }
}

fun Spectre.toSeries(amplitudeName: String, phaseName: String): List<Series<Number, Number>> {
    return listOf(
        Series<Number, Number>().apply {
            name = amplitudeName
            data.addAll(this@toSeries.data().map { Data(it.f, it.a) })
        },
        Series<Number, Number>().apply {
            name = phaseName
            data.addAll(this@toSeries.data().map { Data(it.f, it.phase) })
        }
    )
}

fun Number.toFixed(count: Int): String {
    return DecimalFormat("0." + "#".repeat(count)).format(this)
}
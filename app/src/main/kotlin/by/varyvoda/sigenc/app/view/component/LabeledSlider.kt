package by.varyvoda.sigenc.app.view.component

import by.varyvoda.sigenc.app.view.util.listenValue
import by.varyvoda.sigenc.app.view.util.spacing
import by.varyvoda.sigenc.app.view.util.toFixed
import javafx.event.EventTarget
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import tornadofx.*

class LabeledSlider(min: Double, max: Double, current: Double = (max - min) / 2) : VBox() {

    var valueTransform: (Double) -> String = { it.toString() }
        set(value) {
            field = value
            label.text = valueTransform(slider.value)
        }
    val slider = slider(min, max, current) { hgrow = Priority.ALWAYS }
    val label = label {
        slider.valueProperty().listenValue { _, _, value ->
            text = valueTransform(value.toDouble())
        }
    }

    init {
        spacing()
    }

    fun setNameTransform(name: String, fixedSize: Int? = null) {
        valueTransform = { "$name = ${if (fixedSize == null) it else it.toFixed(fixedSize)}" }
    }

    fun onValue(subscriber: (Double) -> Unit) {
        slider.valueProperty().listenValue { _, _, value -> subscriber(value.toDouble()) }
    }
}

fun EventTarget.labeledSlider(
    min: Double,
    max: Double,
    current: Double = (max - min) / 2,
    init: LabeledSlider.() -> Unit
): LabeledSlider {
    return LabeledSlider(min, max, current).apply(init).also { this@labeledSlider.addChildIfPossible(it) }
}
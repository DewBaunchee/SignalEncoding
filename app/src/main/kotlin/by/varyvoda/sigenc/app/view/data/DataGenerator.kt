package by.varyvoda.sigenc.app.view.data

class DataGenerator {

    companion object {

        fun generateBytes(count: Int): ByteArray {
            return DataGenerator().generateBytes(count)
        }
    }

    fun generateBytes(count: Int): ByteArray {
        return ByteArray(count) { (it % 256).toByte() }
    }
}
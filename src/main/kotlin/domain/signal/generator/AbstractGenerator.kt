package domain.signal.generator

import domain.signal.Signal

abstract class AbstractGenerator(
    protected val n: Int
) : Generator {

    override fun period(count: Int): Signal {
        return Signal(n, (0 until count * n).map { Signal.Part(it, value(it)) })
    }
}
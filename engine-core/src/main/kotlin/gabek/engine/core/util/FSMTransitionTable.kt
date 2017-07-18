package gabek.engine.core.util

/**
 * @author Gabriel Keith
 */
class FSMTransitionTable<T : Enum<T>>(enumSize: Int, private val saveState: (entity: Int, newState: T) -> Unit) {

    private val size: Int = enumSize
    private val matrix: Array<Array<MutableList<(entity: Int, from: T, to: T) -> Unit>>>

    init {
        matrix = Array(size, { Array(size, { mutableListOf<(entity: Int, from: T, to: T) -> Unit>() }) })
    }

    fun addListener(from: T, to: T, listener: (entity: Int, from: T, to: T) -> Unit) {
        matrix[from.ordinal][to.ordinal].add(listener)
    }

    fun addListener(from: Array<T>, to: Array<T>, listener: (entity: Int, from: T, to: T) -> Unit) {
        for (f in from) {
            for (t in to) {
                matrix[f.ordinal][t.ordinal].add(listener)
            }
        }
    }

    fun addListenerLeavening(from: T, listener: (entity: Int, from: T, to: T) -> Unit) {
        for (i in 0 until size) {
            matrix[from.ordinal][i].add(listener)
        }
    }

    fun addListenerEntering(to: T, listener: (entity: Int, from: T, to: T) -> Unit) {
        for (i in 0 until size) {
            matrix[i][to.ordinal].add(listener)
        }
    }

    fun transition(entity: Int, from: T, to: T) {
        for (listener in matrix[from.ordinal][to.ordinal]) {
            listener(entity, from, to)
        }
        saveState(entity, to)
    }
}
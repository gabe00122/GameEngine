package gabek.sm2.tilemap

/**
 * @author Gabriel Keith
 */
interface Grid<T> {
    val w: Int
    val h: Int

    fun get(x: Int, y: Int): T
    fun set(x: Int, y: Int, value: T)
    fun has(x: Int, y: Int): Boolean
    fun iterator(): Iterator<T>
}
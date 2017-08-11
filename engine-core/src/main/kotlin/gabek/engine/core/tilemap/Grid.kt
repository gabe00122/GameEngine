package gabek.engine.core.tilemap

/**
 * @author Gabriel Keith
 */
interface Grid<T> {
    val offsetX: Int
    val offsetY: Int
    val width: Int
    val height: Int

    fun get(x: Int, y: Int): T
    fun set(x: Int, y: Int, value: T)
    fun inBounds(x: Int, y: Int): Boolean
    fun resize(newOffsetX: Int, newOffsetY: Int, newWidth: Int, newHeight: Int)
    operator fun iterator(): Iterator<T>
}
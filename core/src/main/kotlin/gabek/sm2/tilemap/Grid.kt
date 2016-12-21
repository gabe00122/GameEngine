package gabek.sm2.tilemap

/**
 * @author Gabriel Keith
 */
interface Grid<out T> {
    fun get(x: Int, y: Int): T
}
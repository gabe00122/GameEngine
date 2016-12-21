package gabek.sm2.tilemap

import java.util.*

/**
 * @author Gabriel Keith
 */
class ArrayGrid<out T>(val w: Int, val h: Int, defaultValue : (x: Int, y: Int) -> T) : Grid<T>{
    private val tiles = ArrayList<T>(w * h)

    init{
        (0 until w * h).mapTo(tiles){ defaultValue(it % w, it / w) }
    }

    override fun get(x: Int, y: Int): T = tiles[y * w + x]
}
package gabek.sm2.tilemap

import java.util.*

/**
 * @author Gabriel Keith
 */
class ArrayGrid<T>(override val w: Int, override val h: Int, defaultValue : (x: Int, y: Int) -> T) : Grid<T>{

    private val tiles = ArrayList<T>(w * h)

    init{
        (0 until w * h).mapTo(tiles){ defaultValue(it % w, it / w) }
    }

    override fun get(x: Int, y: Int): T = tiles[y * w + x]
    override fun set(x: Int, y: Int, value: T){
        tiles[y * w + x] = value
    }

    override fun has(x: Int, y: Int): Boolean = 0 <= x && x < w && 0 <= y && y < h

    override fun iterator(): Iterator<T> = tiles.iterator()
}
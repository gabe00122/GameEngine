package gabek.engine.core.tilemap

/**
 * @author Gabriel Keith
 */
class ArrayGrid<T>(
        private val defaultValue: (x: Int, y: Int) -> T
) : Grid<T> {

    override var offsetX = 0
        private set
    override var offsetY = 0
        private set

    override var width = 0
        private set
    override var height = 0
        private set

    private var tiles = ArrayList<T>()

    override fun resize(newOffsetX: Int, newOffsetY: Int, newWidth: Int, newHeight: Int){
        val newTiles = ArrayList<T>(newWidth*newHeight)

        for(y in newOffsetY until (newWidth + newOffsetY)){
            for(x in newOffsetX until (newHeight + newOffsetX)){
                newTiles.add(get(x, y))
            }
        }

        tiles = newTiles
        offsetX = newOffsetX
        offsetY = newOffsetY
        width = newWidth
        height = newHeight
    }

    override fun inBounds(x: Int, y: Int) = x >= offsetX && y >= offsetY && x - offsetX < width && y - offsetY < height
    override fun get(x: Int, y: Int): T {
        if(!inBounds(x, y)){
            return defaultValue(x, y)
        } else {
            return tiles[(y - offsetY) * width + (x - offsetX)]
        }
    }
    override fun set(x: Int, y: Int, value: T) {
        tiles[(y - offsetY) * width + (x - offsetX)] = value
    }


    override fun iterator(): Iterator<T> = tiles.iterator()
}

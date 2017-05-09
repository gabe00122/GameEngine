package gabek.engine.tilemap

/**
 * @author Gabriel Keith
 */
class TileDefinitions {
    private val nameToId = mutableMapOf<String, Int>()
    private val tileTypes = mutableListOf<TileType>()

    operator fun get(typeId: Int): TileType {
        return tileTypes[typeId]
    }

    operator fun get(tileReference: TileReference): TileType {
        return tileTypes[tileReference.typeId]
    }

    fun getIdByName(name: String): Int = nameToId[name]!!

    fun addType(type: TileType) {
        nameToId.put(type.name, tileTypes.size)
        tileTypes.add(type)
    }
}
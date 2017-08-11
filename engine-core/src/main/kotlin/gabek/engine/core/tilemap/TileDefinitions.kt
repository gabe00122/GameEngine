package gabek.engine.core.tilemap

/**
 * @author Gabriel Keith
 */
class TileDefinitions {
    private val nameToId = mutableMapOf<String, Int>()
    private val tileTypes = mutableListOf<TileDef>()

    operator fun get(typeId: Int): TileDef {
        return tileTypes[typeId]
    }

    operator fun get(tileInstance: TileInstance): TileDef {
        return tileTypes[tileInstance.typeId]
    }

    fun getIdByName(name: String): Int = nameToId[name]!!

    fun addType(type: TileDef) {
        nameToId.put(type.name, tileTypes.size)
        tileTypes.add(type)
    }
}
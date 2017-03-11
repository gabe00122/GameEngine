package gabek.sm2.tilemap

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets

/**
 * @author Gabriel Keith
 */
class TileDefinitions(val kodein: Kodein) {
    private val assets: Assets = kodein.instance()

    private val nameToId = mutableMapOf<String, Int>()
    private val tileTypes = mutableListOf<TileType>()

    operator fun get(typeId: Int): TileType {
        return tileTypes[typeId]
    }

    operator fun get(tileReference: TileReference): TileType {
        return tileTypes[tileReference.typeId]
    }

    fun getIdByName(name: String): Int = nameToId[name]!!

    fun addType(type: TileType){
        nameToId.put(type.name, tileTypes.size)
        tileTypes.add(type)
    }
}
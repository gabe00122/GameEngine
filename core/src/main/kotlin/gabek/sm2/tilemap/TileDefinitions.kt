package gabek.sm2.tilemap

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets

/**
 * @author Gabriel Keith
 */
class TileDefinitions(val kodein: Kodein) {
    private val assets: Assets = kodein.instance()

    private val tileTypes = mutableListOf<TileType>()

    operator fun get(typeId: Int): TileType {
        return tileTypes[typeId]
    }

    operator fun get(tileReference: TileReference): TileType {
        return tileTypes[tileReference.typeId]
    }

    fun buildNameToIdMap(): Map<String, Int>{
        val out = mutableMapOf<String, Int>()
        for(i in 0 until tileTypes.size){
            out.put(tileTypes[i].name, i)
        }

        return out
    }

    init {
        tileTypes.add(TileType("none", null, false))
        tileTypes.add(TileType("background", assets.findTexture("tiles:back"), false))
        tileTypes.add(TileType("wall", assets.findTexture("tiles:wall"), true))
    }

}
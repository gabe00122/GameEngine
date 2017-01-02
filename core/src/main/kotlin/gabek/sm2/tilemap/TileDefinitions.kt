package gabek.sm2.tilemap

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets

/**
 * @author Gabriel Keith
 */
class TileDefinitions(val kodein: Kodein) {
    private val asstes: Assets = kodein.instance()

    private val tileTypes = mutableListOf<TileType>()

    operator fun get(typeId: Int): TileType {
        return tileTypes[typeId]
    }

    operator fun get(tileReference: TileReference): TileType {
        return tileTypes[tileReference.typeId]
    }

    init {
        tileTypes.add(TileType(asstes.findTexture("tiles", "tiles", 3), false))
        tileTypes.add(TileType(asstes.findTexture("tiles", "tiles", 1), true))
    }

}
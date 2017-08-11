package gabek.onebreath.def

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.tilemap.TileDef
import gabek.engine.core.tilemap.TileDefinitions

/**
 * @author Gabriel Keith
 * @date 7/27/2017
 */
fun tileDef(tileDefinitions: TileDefinitions, world: World, kodein: Kodein) {
    val assets: Assets = kodein.instance()

    tileDefinitions.addType(TileDef("empty"))

    tileDefinitions.addType(TileDef("stone", assets.getTexture("actors:tile:5"), true))
    tileDefinitions.addType(TileDef("water", assets.getTexture("actors:tile:0")))
}
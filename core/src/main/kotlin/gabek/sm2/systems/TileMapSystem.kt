package gabek.sm2.systems

import com.artemis.BaseSystem
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.tilemap.TileMap

/**
 * @author Gabriel Keith
 */
class TileMapSystem(kodein: Kodein) : BaseSystem(){
    val tileMap = TileMap(kodein)


    override fun processSystem() {}


}
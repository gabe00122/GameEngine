package gabek.sm2.systems

import com.artemis.BaseSystem
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.tilemap.TileMap

/**
 * @author Gabriel Keith
 */
class TileMapSystem(kodein: Kodein) : BaseSystem() {
    private lateinit var box2dSystem: Box2dSystem

    val tileMap = TileMap(kodein)

    override fun initialize() {
        tileMap.initPhysics(box2dSystem.box2dWorld)
    }

    override fun processSystem() {}
}
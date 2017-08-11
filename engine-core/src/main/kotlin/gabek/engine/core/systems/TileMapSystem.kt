package gabek.engine.core.systems

import com.artemis.BaseSystem
import com.artemis.World
import com.badlogic.gdx.utils.ObjectMap
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.tilemap.SimpleMeshBuilder
import gabek.engine.core.tilemap.TileMapLayer
import gabek.engine.core.tilemap.TileDefinitions
import gabek.engine.core.util.createIfMissing

/**
 * @author Gabriel Keith
 */
class TileMapSystem(
        val kodein: Kodein,
        val definitionsInit: (TileDefinitions, World, Kodein) -> Unit
) : BaseSystem(){
    val assets: Assets = kodein.instance()

    val pixelToMeters = kodein.instance<PixelRatio>().pixelToMeters
    val tileSize = pixelToMeters * 16f
    val definitions = TileDefinitions()

    private lateinit var box2dSystem: Box2dSystem

    private val layers = ObjectMap<String, TileMapLayer>()
    private val meshBuilders = ArrayList<SimpleMeshBuilder>()


    fun getLayer(name: String): TileMapLayer{
        return layers.createIfMissing(name) {
            val layer = TileMapLayer(definitions, tileSize, pixelToMeters)
            meshBuilders.add(SimpleMeshBuilder(layer))

            layer
        }
    }

    override fun initialize() {
        super.initialize()

        definitionsInit(definitions, world, kodein)
    }

    override fun processSystem() {
        for(i in 0 until meshBuilders.size){
            meshBuilders[i].validateMesh(box2dSystem.rworld)
        }
    }
}
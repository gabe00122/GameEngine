package gabek.sm2.systems

import com.artemis.BaseSystem
import com.badlogic.gdx.utils.JsonValue
import gabek.sm2.leveltemplate.TemplateOperation
import gabek.sm2.physics.RBody
import gabek.sm2.tilemap.ArrayGrid
import gabek.sm2.tilemap.TileReference
import gabek.sm2.world.WorldConfig
import gabek.sm2.world.clear
import gabek.sm2.world.getSystem

/**
 * @author Gabriel Keith
 */
class LevelTemplateLoader : BaseSystem() {
    private lateinit var factoryManager: FactoryManager
    private lateinit var tileSystem: TileMapSystem
    private lateinit var box2dSystem: Box2dSystem
    private lateinit var transSystem: TranslationSystem
    private var operation = HashMap<String, TemplateOperation>()

    override fun initialize() {
        super.initialize()


    }


    override fun processSystem() {}

    fun loadLevel(root: JsonValue, worldConfig: WorldConfig) {
        world.clear()


        val layers = root.get("layers")

        val width = root.getInt("width")
        val height = root.getInt("height")
        val tileWidth = root.getInt("tileWidth")
        val tileHeight = root.getInt("tileHeight")

        val definitions = tileSystem.definitions

        val tileset = root
                .get("tilesets")
                .JsonIterator().first { it.getString("name") == "tiles" }

        val defLookup = IntArray(tileset.getInt("tilecount"))
        for(prop in tileset.get("tileproperties").JsonIterator()){
            defLookup[prop.name.toInt()] = definitions.getIdByName(prop.getString("type"))
        }

        val background = layers
                .JsonIterator()
                .first { it.getString("name") == "background" }

        tileSystem.store()
        tileSystem.resize(width, height)

        var i = 0
        for(tile in background.get("data").JsonIterator()){
            val x = i.rem(width)
            val y = height - i / width
            i++
            val tileData = tile.asInt()

            if(tileData > 0) {
                tileSystem.setTile(x, y,
                        TileMapSystem.Layer.BACKGROUND,
                        TileReference(defLookup[tileData - 1]))
            }
        }

        tileSystem.initPhysics()


        val objects = layers
                .JsonIterator()
                .first { it.getString("name") == "objects" }
                .get("objects")

        for(obj in objects.JsonIterator()){
            val id = factoryManager.create(obj.getString("type"))
            transSystem.teleport(id,
                    (obj.getInt("x")/16f) * tileSystem.tileSize,
                    (height - (obj.getInt("y") - obj.getInt("height"))/16f) * tileSystem.tileSize,
                    0f)
        }

        world.process()
    }
}
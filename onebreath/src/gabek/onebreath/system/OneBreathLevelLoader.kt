package gabek.onebreath.system

import com.artemis.managers.TagManager
import com.badlogic.gdx.utils.IntIntMap
import com.badlogic.gdx.utils.JsonValue
import gabek.engine.core.systems.Box2dSystem
import gabek.engine.core.systems.PassiveSystem
import gabek.engine.core.systems.TileMapSystem
import gabek.engine.core.systems.common.PrefabManager
import gabek.engine.core.systems.common.TranslationSystem
import gabek.engine.core.tilemap.TileReference
import gabek.engine.core.util.clear
import ktx.collections.get

/**
 * @author Gabriel Keith
 */
class OneBreathLevelLoader : PassiveSystem() {
    private lateinit var prefabManager: PrefabManager
    private lateinit var tileSystem: TileMapSystem
    private lateinit var box2dSystem: Box2dSystem
    private lateinit var transSystem: TranslationSystem

    private lateinit var tagManager: TagManager

    override fun processSystem() {}

    fun loadLevel(root: JsonValue) {
        world.clear()


        val layers = root.get("layers")

        val width = root.getInt("width")
        val height = root.getInt("height")
        val tileWidth = root.getInt("tileWidth")
        val tileHeight = root.getInt("tileHeight")

        val definitions = tileSystem.definitions

        val tileset = root
                .get("tilesets")
                .first { it.getString("name") == "tiles" }
        val gid = tileset.getInt("firstgid")

        val defLookup = IntIntMap()
        for (prop in tileset.get("tileproperties")) {
            defLookup.put(prop.name.toInt(), definitions.getIdByName(prop.getString("type")))
        }


        tileSystem.store()
        tileSystem.resize(40, 40)

        loadLayer(layers, TileMapSystem.Layer.BACKGROUND, width, height, defLookup, gid)
        loadLayer(layers, TileMapSystem.Layer.FOREGROUND, width, height, defLookup, gid)

        tileSystem.initPhysics()


        val objects = layers
                .first { it.getString("name") == "objects" }
                .get("objects")

        for (obj in objects.JsonIterator()) {
            val id = prefabManager.create(obj.getString("type"))
            transSystem.teleport(id,
                    (obj.getInt("x") / tileWidth.toFloat()) * tileSystem.tileSize,
                    (height - (obj.getInt("y") - obj.getInt("height")) / tileHeight.toFloat()) * tileSystem.tileSize,
                    0f)

            if (obj.has("properties")) {
                val properties = obj.get("properties")
                if (properties.has("tag")) {
                    tagManager.register(properties.getString("tag"), world.getEntity(id))
                }
            }
        }

        world.process()
    }

    private fun loadLayer(layers: JsonValue, layer: TileMapSystem.Layer, width: Int, height: Int, tileSets: IntIntMap, gid: Int) {
        for ((i, tile) in layers.first { it.getString("name") == layer.name.toLowerCase() }.get("data").withIndex()) {
            val x = i.rem(width)
            val y = height - i / width
            val tileData = tile.asInt()

            if (tileData > 0) {
                tileSystem.setTile(x, y,
                        layer,
                        TileReference(tileSets[tileData - gid]))
            }
        }
    }
}
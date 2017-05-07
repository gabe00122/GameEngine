package gabek.sm2.systems

import com.artemis.managers.GroupManager
import com.badlogic.gdx.utils.JsonValue
import gabek.sm2.systems.common.PrefabManager
import gabek.sm2.systems.common.TranslationSystem
import gabek.sm2.tilemap.TileReference
import gabek.sm2.world.WorldConfig
import gabek.sm2.util.clear

/**
 * @author Gabriel Keith
 */
class LevelTemplateLoader : PassiveSystem() {
    private lateinit var prefabManager: PrefabManager
    private lateinit var tileSystem: TileMapSystem
    private lateinit var box2dSystem: Box2dSystem
    private lateinit var transSystem: TranslationSystem

    private lateinit var groupManager: GroupManager

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
                .first { it.getString("name") == "tiles" }
        val gid = tileset.getInt("firstgid")

        val defLookup = IntArray(tileset.getInt("tilecount"))
        for (prop in tileset.get("tileproperties")) {
            defLookup[prop.name.toInt()] = definitions.getIdByName(prop.getString("type"))
        }


        tileSystem.store()
        tileSystem.resize(width, height)

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
                if (properties.has("group")) {
                    groupManager.add(world.getEntity(id), properties.getString("group"))
                }
            }
        }

        world.process()
    }

    private fun loadLayer(layers: JsonValue, layer: TileMapSystem.Layer, width: Int, height: Int, tileSets: IntArray, gid: Int) {
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
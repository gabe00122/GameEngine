package gabek.engine.core.systems

import com.artemis.World
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.physics.RBody
import gabek.engine.core.physics.RFixture
import gabek.engine.core.tilemap.ArrayGrid
import gabek.engine.core.tilemap.Grid
import gabek.engine.core.tilemap.TileDefinitions
import gabek.engine.core.physics.shape.REdge
import gabek.engine.core.tilemap.TileReference
import gabek.engine.core.systems.common.RenderManager

/**
 * @author Gabriel Keith
 */
class TileMapSystem(
        val kodein: Kodein,
        val definitionsInit: (TileDefinitions, World, Kodein) -> Unit
) : PassiveSystem() {

    private lateinit var box2dSystem: Box2dSystem

    val assets: Assets = kodein.instance()

    val tileSize = 0.75f
    val definitions = TileDefinitions()
    private var backgroundTiles: Grid<TileReference> = ArrayGrid(0, 0) { _, _ -> TileReference(0) }
    private var foregroundTiles: Grid<TileReference> = ArrayGrid(0, 0) { _, _ -> TileReference(0) }

    var body = RBody()

    override fun initialize() {
        super.initialize()

        definitionsInit(definitions, world, kodein)
    }

    fun render(batch: SpriteBatch, culling: Rectangle, layer: Layer) {
        val tiles = when (layer) {
            Layer.FOREGROUND -> foregroundTiles
            Layer.BACKGROUND -> backgroundTiles
        }

        val x1 = MathUtils.clamp(MathUtils.floor(culling.x / tileSize), 0, tiles.w)
        val x2 = MathUtils.clamp(MathUtils.ceil((culling.x + culling.width) / tileSize), 0, tiles.w)

        val y1 = MathUtils.clamp(MathUtils.floor(culling.y / tileSize), 0, tiles.h)
        val y2 = MathUtils.clamp(MathUtils.ceil((culling.y + culling.height) / tileSize), 0, tiles.h)

        for (y in y1 until y2) {
            for (x in x1 until x2) {
                drawTile(batch, tiles, x, y)
            }
        }
    }

    fun getRendererForLayer(layer: Layer): RenderManager.RenderSystem {
        return object : RenderManager.RenderSystem {
            override fun render(batch: SpriteBatch, culling: Rectangle, progress: Float) {
                this@TileMapSystem.render(batch, culling, layer)
            }
        }
    }

    private fun drawTile(batch: SpriteBatch, grid: Grid<TileReference>, x: Int, y: Int) {
        val tile = grid.get(x, y)
        val ref = definitions[tile.typeId].texture
        if (ref != null) {
            batch.draw(ref.texture, x * tileSize, y * tileSize, tileSize, tileSize)
        }
    }

    fun resize(w: Int, h: Int) {
        backgroundTiles = ArrayGrid(w, h) { _, _ -> TileReference(0) }
        foregroundTiles = ArrayGrid(w, h) { _, _ -> TileReference(0) }
    }

    fun setTile(x: Int, y: Int, layer: Layer, reference: TileReference) {
        val grid = if (layer == Layer.FOREGROUND) foregroundTiles else backgroundTiles
        grid.set(x, y, reference)
        definitions[reference].onTileInit(x, y, reference)
    }

    fun initPhysics() {
        for (y in 0 until backgroundTiles.h) {
            for (x in 0 until backgroundTiles.w) {
                if (definitions[backgroundTiles.get(x, y)].solid) {
                    initSolid(x, y)
                }
            }
        }

        box2dSystem.rworld.addBody(body)
    }

    fun store() {
        box2dSystem.rworld.removeBody(body)
        for (y in 0 until backgroundTiles.h) {
            for (x in 0 until backgroundTiles.w) {
                backgroundTiles.get(x, y).fixtures.clear()
            }
        }
    }

    private fun checkSolid(x: Int, y: Int) = backgroundTiles.has(x, y) && definitions[backgroundTiles.get(x, y)].solid

    private fun RFixture.defaultSettings(x: Int, y: Int): RFixture {
        friction = 0.3f
        restitution = 0f
        return this
    }

    private fun initSolid(x: Int, y: Int) {
        val fixtures = backgroundTiles.get(x, y).fixtures

        val n = checkSolid(x, y + 1)
        val s = checkSolid(x, y - 1)
        val e = checkSolid(x + 1, y)
        val w = checkSolid(x - 1, y)

        if (!n || !s || !e || !w) {
            val x1 = tileSize * x
            val y1 = tileSize * y
            val x2 = x1 + tileSize
            val y2 = y1 + tileSize

            if (!n) {
                val edge = REdge()
                edge.set(x1, y2, x2, y2)
                if (w) {
                    edge.setVertex0(x1 - tileSize, y2)
                }
                if (e) {
                    edge.setVertex3(x2 + tileSize, y2)
                }
                val fixture = RFixture(edge).defaultSettings(x, y)
                fixtures.add(fixture)
                body.addFixture(fixture)
            }

            if (!s) {
                val edge = REdge()
                edge.set(x2, y1, x1, y1)
                if (e) {
                    edge.setVertex0(x2 + tileSize, y1)
                }
                if (w) {
                    edge.setVertex3(x1 - tileSize, y1)
                }
                val fixture = RFixture(edge).defaultSettings(x, y)
                fixtures.add(fixture)
                body.addFixture(fixture)
            }

            if (!e) {
                val edge = REdge()
                edge.set(x2, y2, x2, y1)
                if (n) {
                    edge.setVertex0(x2, y2 + tileSize)
                }
                if (s) {
                    edge.setVertex3(x2, y1 - tileSize)
                }
                val fixture = RFixture(edge).defaultSettings(x, y)
                fixtures.add(fixture)
                body.addFixture(fixture)
            }

            if (!w) {
                val edge = REdge()
                edge.set(x1, y1, x1, y2)
                if (s) {
                    edge.setVertex0(x1, y1 - tileSize)
                }
                if (n) {
                    edge.setVertex3(x1, y2 + tileSize)
                }
                val fixture = RFixture(edge).defaultSettings(x, y)
                fixtures.add(fixture)
                body.addFixture(fixture)
            }
        }
    }

    enum class Layer {
        FOREGROUND, BACKGROUND
    }
}
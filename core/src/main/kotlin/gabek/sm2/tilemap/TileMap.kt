package gabek.sm2.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.physics.RBody
import gabek.sm2.physics.REdge
import gabek.sm2.physics.RFixture
import gabek.sm2.world.WALL
import gabek.sm2.world.filter

/**
 * @author Gabriel Keith
 */
class TileMap(val kodein: Kodein) {
    val assets: Assets = kodein.instance()

    val tileSize = 0.75f
    val definitions = TileDefinitions(kodein)
    var tileMap: Grid<TileReference> = ArrayGrid(0, 0, { x, y -> TileReference(0) })
    var body = RBody()

    fun render(batch: SpriteBatch, culling: Rectangle) {
        val x1 = MathUtils.clamp(MathUtils.floor(culling.x / tileSize), 0, tileMap.w)
        val x2 = MathUtils.clamp(MathUtils.ceil((culling.x + culling.width) / tileSize), 0, tileMap.w)

        val y1 = MathUtils.clamp(MathUtils.floor(culling.y / tileSize), 0, tileMap.h)
        val y2 = MathUtils.clamp(MathUtils.ceil((culling.y + culling.height) / tileSize), 0, tileMap.h)

        for (y in y1 until y2) {
            for (x in x1 until x2) {
                val tile = tileMap.get(x, y)
                val ref = definitions[tile.typeId].texture
                if(ref != null) {
                    batch.draw(assets.retrieveRegion(ref), x * tileSize, y * tileSize, tileSize, tileSize)
                }
            }
        }
    }

    fun initPhysics(box2dWorld: World) {
        for (y in 0 until tileMap.h) {
            for (x in 0 until tileMap.w) {
                if (definitions[tileMap.get(x, y)].solid) {
                    initSolid(x, y)
                }
            }
        }

        body.initialise(box2dWorld)
    }

    fun store(box2dWorld: World) {
        body.store(box2dWorld)
        for (y in 0 until tileMap.h) {
            for (x in 0 until tileMap.w) {
                tileMap.get(x, y).fixtures = null
            }
        }
    }

    private fun checkSolid(x: Int, y: Int) = tileMap.has(x, y) && definitions[tileMap.get(x, y)].solid

    private fun RFixture.defaultSettings(x: Int, y: Int): RFixture {
        friction = 1f
        restitution = 0f
        categoryBits = filter(WALL)
        return this
    }

    private fun initSolid(x: Int, y: Int) {
        val fixtures = Array<RFixture?>(4, { null })

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
                fixtures[0] = RFixture(edge).defaultSettings(x, y)
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
                fixtures[1] = RFixture(edge).defaultSettings(x, y)
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
                fixtures[2] = RFixture(edge).defaultSettings(x, y)
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
                fixtures[3] = RFixture(edge).defaultSettings(x, y)
            }

            for (fixture in fixtures) {
                if (fixture != null) {
                    body.addFixture(fixture)
                }
            }
            tileMap.get(x, y).fixtures = fixtures
        }
    }
}
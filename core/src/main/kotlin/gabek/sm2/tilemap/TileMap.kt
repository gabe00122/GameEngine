package gabek.sm2.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.World
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.physics.RBody
import gabek.sm2.physics.REdge
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class TileMap(val kodein: Kodein) {
    val tileSize = 0.75f
    val definitions = TileDefinitions(kodein)
    val grid: Grid<TileReference> = ArrayGrid(50, 20, { x, y -> TileReference(0) })

    val body = RBody()

    init {
        for (x in 0 until grid.w) {
            grid.set(x, 0, TileReference(1))
            grid.set(x, grid.h - 1, TileReference(1))
        }

        for(y in 0 until grid.h){
            grid.set(0, y, TileReference(1))
            grid.set(grid.w - 1, y, TileReference(1))
        }

        for (x in 3..8) {
            grid.set(x, 3, TileReference(1))
        }

        for (x in 8..12) {
            grid.set(x, 9, TileReference(1))
        }

        for (x in 2..11) {
            grid.set(x, 13, TileReference(1))
        }
    }


    fun render(batch: SpriteBatch, culling: Rectangle) {
        val x1 = MathUtils.clamp(MathUtils.floor(culling.x / tileSize), 0, grid.w)
        val x2 = MathUtils.clamp(MathUtils.ceil((culling.x + culling.width) / tileSize), 0, grid.w)

        val y1 = MathUtils.clamp(MathUtils.floor(culling.y / tileSize), 0, grid.h)
        val y2 = MathUtils.clamp(MathUtils.ceil((culling.y + culling.height) / tileSize), 0 , grid.h)

        for (y in y1 until y2) {
            for (x in x1 until x2) {
                val tile = grid.get(x, y)
                val type = definitions[tile.typeId]
                batch.draw(type.texture, x * tileSize, y * tileSize, tileSize, tileSize)
            }
        }
    }

    fun initPhysics(box2dWorld: World) {
        for (y in 0 until grid.h) {
            for (x in 0 until grid.w) {
                if (definitions[grid.get(x, y)].solid) {
                    initSolid(x, y)
                }
            }
        }

        body.initialise(box2dWorld)
    }

    fun store(box2dWorld: World) {
        body.store(box2dWorld)
        for (y in 0 until grid.h) {
            for (x in 0 until grid.w) {
                grid.get(x, y).fixtures = null
            }
        }
    }

    private fun checkSolid(x: Int, y: Int) = grid.has(x, y) && definitions[grid.get(x, y)].solid

    private fun RFixture.defaultSettings(x: Int, y: Int): RFixture {
        friction = 1f
        restitution = 0f
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
            grid.get(x, y).fixtures = fixtures
        }
    }
}
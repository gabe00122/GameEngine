package gabek.sm2.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.physics.RBody
import gabek.sm2.physics.REdge
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class TileMap(val kodein: Kodein) {
    val tileSize = 1f
    val definitions = TileDefinitions(kodein)
    val grid: Grid<TileReference> = ArrayGrid(10, 10, {x, y -> TileReference(0)})

    val body = RBody()

    init{
        for(x in 0 until grid.w){
            grid.set(x, 0, TileReference(1))
        }
    }


    fun render(batch: SpriteBatch){
        for(y in 0 until grid.h) {
            for(x in 0 until grid.w) {
                val tile = grid.get(x, y)
                val type = definitions[tile.typeId]
                batch.draw(type.texture, x * tileSize, y * tileSize, tileSize, tileSize)
            }
        }
    }

    fun initPhysics(box2dWorld: World){
        for(y in 0 until grid.h){
            for(x in 0 until grid.w){
                if(definitions[grid.get(x, y)].solid){
                    initSolid(x, y)
                }
            }
        }

        body.initialise(box2dWorld)
    }

    fun store(box2dWorld: World){
        body.store(box2dWorld)
        for(y in 0 until grid.h){
            for(x in 0 until grid.w){
                grid.get(x, y).fixtures = null
            }
        }
    }

    private fun checkSolid(x: Int, y: Int) = grid.has(x, y) && definitions[grid.get(x, y)].solid

    private fun RFixture.defaultSettings(): RFixture{
        friction = 1f
        restitution = 0f
        return this
    }

    private fun initSolid(x: Int, y: Int) {
        val fixtures = Array<RFixture?>(4, {null})

        val n = checkSolid(x, y+1)
        val s = checkSolid(x, y-1)
        val e = checkSolid(x+1, y)
        val w = checkSolid(x-1, y)

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
                fixtures[0] = RFixture(edge).defaultSettings()
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
                fixtures[1] = RFixture(edge).defaultSettings()
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
                fixtures[2] = RFixture(edge).defaultSettings()
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
                fixtures[3] = RFixture(edge).defaultSettings()
            }

            for(fixture in fixtures){
                if(fixture != null) {
                    body.addFixture(fixture)
                }
            }
            grid.get(x, y).fixtures = fixtures
        }
    }
}
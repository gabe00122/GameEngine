package gabek.engine.core.tilemap

import gabek.engine.core.physics.RBody
import gabek.engine.core.physics.RFixture
import gabek.engine.core.physics.RWorld
import gabek.engine.core.physics.shape.REdge

/**
 * @author Gabriel Keith
 * @date 7/27/2017
 */
class SimpleMeshBuilder(layer: TileMapLayer): TileMapLayer.TileChangeListener {

    private val grid = layer.grid
    private val definitions = layer.definitions
    private val tileSize = layer.tileSize

    private val body = RBody() //TODO gotta be a better way
    private var dirty = true

    init{
        layer.listener = this
    }

    override fun onTileSet(x: Int, y: Int, value: TileInstance) {
        dirty = true //TODO only check the tiles that were changed
    }

    fun initPhysics(rworld: RWorld) {
        for(y in grid.offsetY until (grid.height + grid.offsetY)){
            for(x in grid.offsetX until (grid.width + grid.offsetX)){
                val instance = grid.get(x, y)
                if (instance != null && definitions[instance].solid) {
                    initSolid(instance, x, y)
                }
            }
        }

        rworld.addBody(body)
    }

    fun store(rworld: RWorld){
        rworld.removeBody(body)
        for(instance in grid){
            instance?.fixtures?.clear()
        }
    }

    fun validateMesh(rworld: RWorld) {
        if(dirty){
            dirty = false
            store(rworld)
            initPhysics(rworld)
        }
    }

    private fun checkSolid(x: Int, y: Int): Boolean{
        val instance = grid.get(x, y)

        if(instance != null) {
            return definitions[instance].solid
        } else {
            return false
        }
    }

    private fun RFixture.defaultSettings(x: Int, y: Int): RFixture {
        friction = 0.3f
        restitution = 0f
        return this
    }

    private fun initSolid(instance: TileInstance, x: Int, y: Int) {
        val fixtures = instance.fixtures

        val n = checkSolid(x, y + 1)
        val s = checkSolid(x, y - 1)
        val e = checkSolid(x + 1, y)
        val w = checkSolid(x - 1, y)

        if (!n || !s || !e || !w) {
            val x1 = tileSize * x// + 0.01f
            val y1 = tileSize * y// + 0.01f
            val x2 = x1 + tileSize// - 0.02f
            val y2 = y1 + tileSize// - 0.02f

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
}
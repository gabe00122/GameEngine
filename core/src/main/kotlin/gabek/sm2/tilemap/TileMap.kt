package gabek.sm2.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 */
class TileMap(val kodein: Kodein) {
    val tileSize = 1f
    val definitions = TileDefinitions(kodein)
    val grid: Grid<TileReference> = ArrayGrid(10, 10, {x, y -> TileReference(x % 2)})

    fun render(batch: SpriteBatch){

        for(y in 0 until 10) {
            for(x in 0 until 10) {
                val tile = grid.get(x, y)
                val type = definitions[tile.typeId]
                batch.draw(type.texture, x * tileSize, y * tileSize, tileSize, tileSize)
            }
        }
    }
}
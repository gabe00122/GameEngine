package gabek.engine.core.tilemap

import gabek.engine.core.graphics.Sprite

/**
 * @author Gabriel Keith
 */
open class TileType(val name: String,
                    val texture: Sprite? = null,
                    val solid: Boolean = false) {

    open fun onTileInit(x: Int, y: Int, reference: TileReference) {}
}
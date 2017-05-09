package gabek.engine.tilemap

import gabek.engine.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
open class TileType(val name: String,
                    val texture: TextureRef? = null,
                    val solid: Boolean = false) {

    open fun onTileInit(x: Int, y: Int, reference: TileReference) {}
}
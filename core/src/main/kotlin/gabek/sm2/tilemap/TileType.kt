package gabek.sm2.tilemap

import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
class TileType(val name: String,
               val texture: TextureRef? = null,
               val solid: Boolean = false,
               val onInit: ((x: Int, y: Int, reference: TileReference) -> Unit)? = null)
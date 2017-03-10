package gabek.sm2.tilemap

import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
class TileType(val name: String,
               val onInit: ((reference: TileReference, x: Int, y: Int) -> Unit)?,
               val texture: TextureRef?,
               val solid: Boolean = false)
package gabek.sm2.tilemap

import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
data class TileType(val name: String, val texture: TextureRef?, val solid: Boolean = false)
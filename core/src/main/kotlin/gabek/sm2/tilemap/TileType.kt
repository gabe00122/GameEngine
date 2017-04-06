package gabek.sm2.tilemap

import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
open class TileType(val name: String,
               val texture: TextureRef? = null,
               val solid: Boolean = false){

    open fun onTileInit(x: Int, y: Int, reference: TileReference){}
}
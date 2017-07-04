package gabek.engine.core.components.graphics

import gabek.engine.core.components.RComponent
import gabek.engine.core.assets.TextureRef

/**
 * @author Gabriel Keith
 * @date 4/15/2017
 */
class ParallaxGraphicCom : RComponent<ParallaxGraphicCom>() {
    var sprite: TextureRef? = null

    var scrollFactorX: Float = 0f
    var scrollFactorY: Float = 0f
    var width: Float = 0f
    var height: Float = 0f

    override fun set(other: ParallaxGraphicCom) {
        sprite = other.sprite

        scrollFactorX = other.scrollFactorX
        scrollFactorY = other.scrollFactorY
        width = other.width
        height = other.height
    }

    override fun reset() {
        sprite = null

        scrollFactorX = 0f
        scrollFactorY = 0f
        width = 0f
        height = 0f
    }

    override fun toString() = "ParallaxGraphicCom: sprite = $sprite, scrollFactorX = $scrollFactorX, scrollFactorY = $scrollFactorY, width = $width, height = $height"
}
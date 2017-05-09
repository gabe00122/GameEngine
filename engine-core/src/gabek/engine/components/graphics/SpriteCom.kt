package gabek.engine.components.graphics

import com.badlogic.gdx.graphics.Color
import gabek.engine.components.RComponent
import gabek.engine.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
class SpriteCom : RComponent<SpriteCom>() {
    var textureRef: TextureRef? = null

    var width = 0f
    var height = 0f
    var flipX: Boolean = false
    var flipY: Boolean = false
    var offsetX: Float = 0f
    var offsetY: Float = 0f
    var offsetRotation: Float = 0f

    var tint: Color = Color.WHITE

    var layer: Int = 4

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    override fun set(other: SpriteCom) {
        textureRef = other.textureRef
        width = other.width
        height = other.height
        flipX = other.flipX
        flipY = other.flipY
        offsetX = other.offsetX
        offsetY = other.offsetY
        offsetRotation = other.offsetRotation

        tint = other.tint
        layer = other.layer
    }

    override fun reset() {
        textureRef = null
        width = 0f
        height = 0f
        flipX = false
        flipY = false
        offsetX = 0f
        offsetY = 0f
        offsetRotation = 0f

        tint = Color.WHITE
        layer = 4
    }

    override fun toString() = "SpriteCom"
}
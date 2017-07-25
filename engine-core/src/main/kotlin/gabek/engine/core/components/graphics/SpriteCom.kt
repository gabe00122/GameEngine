package gabek.engine.core.components.graphics

import com.badlogic.gdx.graphics.Color
import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.Sprite

/**
 * @author Gabriel Keith
 */
class SpriteCom: RComponent<SpriteCom>() {
    var sprite: Sprite? = null

    var flipX: Boolean = false
    var flipY: Boolean = false
    var offsetX: Float = 0f
    var offsetY: Float = 0f
    var offsetRotation: Float = 0f

    var tint: Color = Color.WHITE

    var layer: Int = 4

    override fun set(other: SpriteCom) {
        sprite = other.sprite
        flipX = other.flipX
        flipY = other.flipY
        offsetX = other.offsetX
        offsetY = other.offsetY
        offsetRotation = other.offsetRotation

        tint = other.tint
        layer = other.layer
    }

    override fun reset() {
        sprite = null
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
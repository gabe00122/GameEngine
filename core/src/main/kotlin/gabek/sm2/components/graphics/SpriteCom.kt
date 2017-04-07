package gabek.sm2.components.graphics

import com.artemis.Component
import com.badlogic.gdx.graphics.Color
import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
class SpriteCom : Component() {
    var textureRef = TextureRef.NONE

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
}
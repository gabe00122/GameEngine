package gabek.sm2.components.graphics

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * @author Gabriel Keith
 */
class SpriteCom : Component() {
    @Transient var texture: TextureRegion? = null

    var width = 0f
    var height = 0f
    var flipX: Boolean = false
    var flipY: Boolean = false
    var offsetX: Float = 0f
    var offsetY: Float = 0f

    fun setSize(width: Float, height: Float){
        this.width = width
        this.height = height
    }
}
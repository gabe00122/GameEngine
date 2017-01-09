package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import com.badlogic.gdx.math.MathUtils

/**
 * @author Gabriel Keith
 */
class CameraCom : Component() {
    var viewportWidth: Float = 0f
    var viewportHeight: Float = 0f

    var pViewportWidth: Float = 0f
    var pViewportHeight: Float = 0f

    fun lerpViewportWidth(progress: Float) = MathUtils.lerp(pViewportWidth, viewportWidth, progress)
    fun lerpViewportHeight(progress: Float) = MathUtils.lerp(pViewportHeight, viewportHeight, progress)
}
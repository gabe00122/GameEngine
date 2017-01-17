package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import com.badlogic.gdx.math.MathUtils

/**
 * @author Gabriel Keith
 */
class TranslationCom : Component() {
    var x: Float = 0f
    var y: Float = 0f
    var rotation: Float = 0f

    var pX: Float = 0f
    var pY: Float = 0f
    var pRotation: Float = 0f

    fun initPos(x: Float, y: Float){
        this.x = x
        this.pX = x
        this.y = y
        this.pY = y
    }

    fun lerpX(progress: Float) = MathUtils.lerp(pX, x, progress)
    fun lerpY(progress: Float) = MathUtils.lerp(pY, y, progress)
    fun lerpRotation(progress: Float) = MathUtils.lerp(pRotation, rotation, progress)
}
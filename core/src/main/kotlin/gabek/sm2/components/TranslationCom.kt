package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import com.badlogic.gdx.math.MathUtils

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class TranslationCom : Component(){
    @JvmField var x: Float = 0f
    @JvmField var y: Float = 0f
    @JvmField var rotation: Float = 0f

    @JvmField var pX: Float = 0f
    @JvmField var pY: Float = 0f
    @JvmField var pRotation: Float = 0f

    fun lerpX(progress: Float) = MathUtils.lerp(pX, x, progress)
    fun lerpY(progress: Float) = MathUtils.lerp(pY, y, progress)
    fun lerpRotation(progress: Float) = MathUtils.lerp(pRotation, rotation, progress)
}
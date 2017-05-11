package gabek.engine.core.components.common

import com.badlogic.gdx.math.MathUtils
import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */
class TranslationCom : RComponent<TranslationCom>() {
    var x: Float = 0f
    var y: Float = 0f
    var rotation: Float = 0f

    var pX: Float = 0f
    var pY: Float = 0f
    var pRotation: Float = 0f

    fun initPos(x: Float, y: Float) {
        this.x = x
        this.pX = x
        this.y = y
        this.pY = y
    }

    fun lerpX(progress: Float) = MathUtils.lerp(pX, x, progress)
    fun lerpY(progress: Float) = MathUtils.lerp(pY, y, progress)
    fun lerpRotation(progress: Float) = MathUtils.lerp(pRotation, rotation, progress)

    override fun set(other: TranslationCom) {
        x = other.x
        y = other.y
        rotation = other.rotation

        pX = other.pX
        pY = other.pY
        pRotation = other.pRotation
    }

    override fun reset() {
        x = 0f
        y = 0f
        rotation = 0f

        pX = 0f
        pY = 0f
        pRotation = 0f
    }

    override fun toString() = "TranslationCom: x = $x, y = $y, rotation = $rotation"
}
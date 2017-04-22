package gabek.sm2.util

import com.badlogic.gdx.math.MathUtils

/**
 * @author Gabriel Keith
 */
class LerpVector(var x: Float = 0f, var y: Float = 0f) {
    private var pX: Float = x
    private var pY: Float = y

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun set(other: LerpVector){
        x = other.x
        y = other.y
        pX = other.pX
        pY = other.pY
    }

    fun lerpX(progress: Float) = MathUtils.lerp(pX, x, progress)
    fun lerpY(progress: Float) = MathUtils.lerp(pY, y, progress)

    fun flip() {
        pX = x
        pY = y
    }

    fun reset() {
        x = 0f
        y = 0f
        pX = 0f
        pY = 0f
    }
}
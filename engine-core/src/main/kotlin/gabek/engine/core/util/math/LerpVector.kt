package gabek.engine.core.util.math

import gabek.engine.core.util.entity.Mirrorable

/**
 * @author Gabriel Keith
 */
class LerpVector(var x: Float = 0f, var y: Float = 0f): Mirrorable<LerpVector> {
    private var pX: Float = x
    private var pY: Float = y

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    override fun set(other: LerpVector) {
        x = other.x
        y = other.y
        pX = other.pX
        pY = other.pY
    }

    fun lerpX(progress: Float) = pX + (x - pX) * progress
    fun lerpY(progress: Float) = pY + (y - pY) * progress

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
package gabek.engine.core.util

import com.badlogic.gdx.math.MathUtils

/**
 * @author Gabriel Keith
 */
class LerpDegree(var degree: Float = 0f) {
    private var pDegree: Float = degree

    fun learDegree(progress: Float) = MathUtils.lerpAngleDeg(pDegree, degree, progress)

    fun flip() {
        pDegree = degree
    }

    fun reset() {
        degree = 0f
        pDegree = 0f
    }
}
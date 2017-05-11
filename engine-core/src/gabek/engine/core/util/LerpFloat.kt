package gabek.engine.core.util

import com.badlogic.gdx.math.MathUtils

/**
 * @author Gabriel Keith
 */
class LerpFloat(var value: Float = 0f) {
    private var pValue = value

    fun flip() {
        pValue = value
    }

    fun lerp(progress: Float): Float {
        return MathUtils.lerp(pValue, value, progress)
    }

    fun reset() {
        value = 0f
        pValue = 0f
    }
}

package gabek.sm2.components.common

import com.badlogic.gdx.math.MathUtils
import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class BoundCom : RComponent<BoundCom>() {
    var width = 0f
    var height = 0f

    var pWidth = 0f
    var pHeight = 0f

    fun lerpWidth(progress: Float) = MathUtils.lerp(pWidth, width, progress)
    fun lerpHeight(progress: Float) = MathUtils.lerp(pHeight, height, progress)

    override fun set(other: BoundCom) {
        width = other.width
        height = other.height

        pWidth = other.pWidth
        pHeight = other.pHeight
    }

    override fun reset() {
        width = 0f
        height = 0f

        pWidth = 0f
        pHeight = 0f
    }

    override fun toString(): String {
        return "BoundCom: width = $width, height = $height"
    }
}
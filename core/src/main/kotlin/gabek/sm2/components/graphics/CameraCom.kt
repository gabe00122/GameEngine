package gabek.sm2.components.graphics

import gabek.sm2.components.RComponent
import gabek.sm2.util.LerpVector

/**
 * @author Gabriel Keith
 */
class CameraCom : RComponent<CameraCom>() {
    var bottomLeft = LerpVector()
    var topRight = LerpVector()

    var aspectRatio: Float = 0f

    override fun set(other: CameraCom) {
        bottomLeft.set(other.bottomLeft)
        topRight.set(other.topRight)

        aspectRatio = other.aspectRatio
    }

    override fun reset() {
        bottomLeft.reset()
        topRight.reset()

        aspectRatio = 0f
    }

    override fun toString() = "CameraCom"
}
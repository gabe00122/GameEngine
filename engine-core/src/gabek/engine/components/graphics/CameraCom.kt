package gabek.sm2.components.graphics

import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 */
class CameraCom : RComponent<CameraCom>() {
    var aspectRatio: Float = 0f

    override fun set(other: CameraCom) {
        aspectRatio = other.aspectRatio
    }

    override fun reset() {
        aspectRatio = 0f
    }

    override fun toString() = "CameraCom: aspectRatio = $aspectRatio"
}
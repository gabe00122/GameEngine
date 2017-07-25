package gabek.engine.core.components.graphics

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */
class CameraCom : RComponent<CameraCom>() {
    var pixelAlignment: Float = -1f

    override fun set(other: CameraCom) {
        pixelAlignment = other.pixelAlignment
    }

    override fun reset() {
        pixelAlignment = -1f
    }

    override fun toString() = "CameraCom: pixelAlignment = $pixelAlignment"
}
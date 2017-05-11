package gabek.engine.core.components.common

import gabek.engine.core.components.RComponent


/**
 * @author Gabriel Keith
 */
class ParentOffsetCom : RComponent<ParentOffsetCom>() {
    var x: Float = 0f
    var y: Float = 0f

    override fun set(other: ParentOffsetCom) {
        x = other.x
        y = other.y
    }

    override fun reset() {
        x = 0f
        y = 0f
    }

    override fun toString() = "ParentOffsetCom: x = $x, y = $y"
}
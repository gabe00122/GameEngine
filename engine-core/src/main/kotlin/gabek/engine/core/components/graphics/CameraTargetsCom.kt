package gabek.engine.core.components.graphics

import com.artemis.annotations.EntityId
import com.artemis.annotations.LinkPolicy
import com.artemis.utils.IntBag
import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */
class CameraTargetsCom : RComponent<CameraTargetsCom>() {
    @JvmField @EntityId
    var target: Int = -1

    override fun set(other: CameraTargetsCom) {
        target = other.target
    }

    override fun reset() {
        target = -1
    }

    override fun toString() = "CameraTargetsCom: target = $target"
}
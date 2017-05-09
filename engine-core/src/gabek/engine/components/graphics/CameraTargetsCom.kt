package gabek.engine.components.graphics

import com.artemis.annotations.EntityId
import com.artemis.annotations.LinkPolicy
import com.artemis.utils.IntBag
import gabek.engine.components.RComponent

/**
 * @author Gabriel Keith
 */
class CameraTargetsCom : RComponent<CameraTargetsCom>() {
    @JvmField @EntityId @LinkPolicy(LinkPolicy.Policy.CHECK_SOURCE_AND_TARGETS)
    val targets: IntBag = IntBag()

    var padWidth = 0f
    var padHeight = 0f

    override fun set(other: CameraTargetsCom) {
        targets.clear()
        targets.addAll(other.targets)

        padWidth = other.padWidth
        padHeight = other.padHeight
    }

    override fun reset() {
        targets.clear()

        padWidth = 0f
        padHeight = 0f
    }

    override fun toString() = "CameraTargetsCom"
}
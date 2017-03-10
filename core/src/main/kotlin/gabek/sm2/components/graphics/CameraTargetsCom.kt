package gabek.sm2.components.graphics

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.annotations.LinkPolicy
import com.artemis.utils.IntBag

/**
 * @author Gabriel Keith
 */
class CameraTargetsCom : Component() {
    @JvmField @EntityId @LinkPolicy(LinkPolicy.Policy.CHECK_SOURCE_AND_TARGETS)
    val targets: IntBag = IntBag()

    var padWidth = 0f
    var padHeight = 0f
}
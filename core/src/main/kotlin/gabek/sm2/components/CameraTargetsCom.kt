package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.utils.IntBag

/**
 * @author Gabriel Keith
 */
class CameraTargetsCom : Component(){
    @EntityId @JvmField
    val targets = IntBag()

    var padWidth = 5f
    var padHeight = 5f
}
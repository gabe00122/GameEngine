package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.annotations.PooledWeaver
import com.artemis.utils.IntBag

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class CameraTargetsCom : Component() {
    @JvmField @EntityId val targets = IntBag()

    @JvmField var padWidth = 5f
    @JvmField var padHeight = 5f
}
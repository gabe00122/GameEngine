package gabek.sm2.components.character

import com.artemis.PooledComponent
import com.artemis.annotations.EntityId
import gabek.sm2.physics.joints.RRevoluteJoint

/**
 * @author Gabriel Keith
 */
class MovementPhysicsWheelCom : PooledComponent() {
    @JvmField
    @EntityId var wheelRef = -1

    var motor: RRevoluteJoint = RRevoluteJoint()

    override fun reset() {
        wheelRef = -1
        motor = RRevoluteJoint()
    }
}
package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.DelayedComponentRemoval
import com.artemis.annotations.PooledWeaver
import gabek.sm2.physics.RBody
import gabek.sm2.physics.joints.RRevoluteJoint

/**
 * @author Gabriel Keith
 */
@DelayedComponentRemoval
class CharacterPeripheryCom : Component() {
    @JvmField var body = RBody()
    @JvmField var motor = RRevoluteJoint()
}
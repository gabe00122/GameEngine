package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import gabek.sm2.physics.RBody
import gabek.sm2.physics.joints.RRevoluteJoint

/**
 * @author Gabriel Keith
 */
//@DelayedComponentRemoval
@PooledWeaver
class CharacterPeripheryCom : Component() {
    var body = RBody()
    var motor = RRevoluteJoint()
}
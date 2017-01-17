package gabek.sm2.components

import com.artemis.Component
import gabek.sm2.physics.RBody
import gabek.sm2.physics.joints.RRevoluteJoint

/**
 * @author Gabriel Keith
 */
class CharacterStateCom : Component() {
    var legsBody = RBody()
    var legsMotor = RRevoluteJoint()

    var direction: Direction = Direction.RIGHT
    var running = false

    var onGround = false
    var wasOnGround = false



    var jumpTimeOut: Float = 0f

    enum class Direction {
        LEFT, RIGHT
    }
}
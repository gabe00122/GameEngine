package gabek.sm2.components

import com.artemis.Component
import com.badlogic.gdx.math.Vector2
import gabek.sm2.physics.RBody
import gabek.sm2.physics.RFixture
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
    var groundFixture: RFixture? = null
    var groundContactPoint = Vector2()


    var jumpTimeOut: Float = 0f

    enum class Direction {
        LEFT, RIGHT
    }
}
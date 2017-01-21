package gabek.sm2.components.character

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.badlogic.gdx.math.Vector2
import gabek.sm2.physics.RBody
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.joints.RRevoluteJoint

/**
 * @author Gabriel Keith
 */
class CharacterStateCom : Component() {
    @JvmField @EntityId var legChild: Int = -1
    var legsMotor = RRevoluteJoint()

    var direction: Direction = Direction.RIGHT
    var lateralMovement = false

    var onGround = false
    var groundFixture: RFixture? = null
    var groundContactPoint = Vector2()


    var jumpTimeOut: Float = 0f

    enum class Direction {
        LEFT, RIGHT
    }
}
package gabek.sm2.components.character

import com.artemis.Component
import com.artemis.PooledComponent
import com.artemis.annotations.EntityId
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import gabek.sm2.physics.RContact
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.joints.RRevoluteJoint

/**
 * @author Gabriel Keith
 */
class MovementPhysicsCom: Component(){
    @JvmField
    @EntityId var wheelRef = -1

    var motor: RRevoluteJoint = RRevoluteJoint()

    val groundPoint = Vector2()
    var groundFixture: RFixture? = null

    //override fun reset() {
    //    wheelRef = -1
    //    motor = RRevoluteJoint()
    //    groundPoint.setZero()
    //    groundFixture = null
    //}
}
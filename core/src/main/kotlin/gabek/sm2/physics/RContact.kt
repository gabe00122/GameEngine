package gabek.sm2.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact

/**
 * @author Gabriel Keith
 */
class RContact(val body: RBody, val fixture: RFixture, val otherId: Int, val otherBody: RBody, val otherFixture: RFixture) {
    var hasManifold: Boolean = false
    var numberOfPoints: Int = 0
    var points: Array<Vector2> = arrayOf(Vector2(), Vector2())

    fun update(contact: Contact){
        val manifold = contact.worldManifold

        hasManifold = !fixture.isSensor && !otherFixture.isSensor

        if(hasManifold) {
            numberOfPoints = manifold.numberOfContactPoints
            points[0].set(manifold.points[0])
            points[1].set(manifold.points[1])
        }
    }
}
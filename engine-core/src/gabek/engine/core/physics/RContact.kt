package gabek.engine.core.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact

/**
 * @author Gabriel Keith
 */
class RContact {
    var fixture: RFixture? = null
    var otherFixture: RFixture? = null

    var hasManifold: Boolean = false
    var numberOfPoints: Int = 0
    var points: Array<Vector2> = arrayOf(Vector2(), Vector2())

    fun update(contact: Contact, fixture: RFixture, otherFixture: RFixture) {
        this.fixture = fixture
        this.otherFixture = otherFixture

        val manifold = contact.worldManifold

        hasManifold = !fixture.isSensor && !otherFixture.isSensor

        if (hasManifold) {
            numberOfPoints = manifold.numberOfContactPoints
            points[0].set(manifold.points[0])
            points[1].set(manifold.points[1])
        }
    }
}
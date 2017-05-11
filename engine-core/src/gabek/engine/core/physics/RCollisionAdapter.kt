package gabek.engine.core.physics

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold

/**
 * @author Gabriel Keith
 * @date 3/12/2017
 */
abstract class RCollisionAdapter : RCollisionCallback {
    override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {}

    override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {}

    override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {}

    override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {}

}
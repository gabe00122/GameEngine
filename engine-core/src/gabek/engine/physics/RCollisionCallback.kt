package gabek.sm2.physics

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold

/**
 * @author Gabriel Keith
 */
interface RCollisionCallback {
    fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture)
    fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture)

    fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture)
    fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture)
}
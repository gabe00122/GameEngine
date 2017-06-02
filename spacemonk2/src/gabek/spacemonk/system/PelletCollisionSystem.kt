package gabek.engine.core.systems.pellet

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.pellet.PelletCollisionCom
import gabek.engine.core.physics.RCollisionCallback
import gabek.engine.core.physics.RFixture
import gabek.engine.core.systems.character.DamageSystem

/**
 * @author Gabriel Keith
 */

class PelletCollisionSystem: BaseEntitySystem(Aspect.all(PelletCollisionCom::class.java, BodyCom::class.java)) {
    private lateinit var pelletCollisionMapper: ComponentMapper<PelletCollisionCom>
    private lateinit var damageSystem: DamageSystem
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    override fun processSystem() {}


    override fun inserted(entityId: Int) {
        bodyMapper[entityId].body.addCallbackToAll(collisionCallback)
    }

    private val collisionCallback = object: RCollisionCallback {
        override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val owner = ownerRFixture.ownerId
            val other = otherRFixture.ownerId

            val effect = pelletCollisionMapper[owner]
            val hasHealth = damageSystem.hasHealth(other)

            if (hasHealth) {
                damageSystem.damage(other, effect.damage)
            }

            if ((hasHealth && effect.diesOnAttack) || effect.diesOnCollision) {
                world.delete(owner)
            }
        }

        override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {}
        override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {}
        override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {}
    }

}
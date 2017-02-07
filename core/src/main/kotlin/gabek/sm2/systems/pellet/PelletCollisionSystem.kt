package gabek.sm2.systems.pellet

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import gabek.sm2.components.BodyCom
import gabek.sm2.components.HealthCom
import gabek.sm2.components.ParentOfCom
import gabek.sm2.components.pellet.PelletCollisionCom
import gabek.sm2.physics.RCollisionCallback
import gabek.sm2.physics.RFixture
import gabek.sm2.systems.ParentSystem
import gabek.sm2.util.RecursiveMapper

/**
 * @author Gabriel Keith
 */

class PelletCollisionSystem: BaseEntitySystem(Aspect.all(PelletCollisionCom::class.java, BodyCom::class.java)){
    private lateinit var pelletCollisionMapper: ComponentMapper<PelletCollisionCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    private lateinit var recursiveHealthMapper: RecursiveMapper<HealthCom>

    override fun initialize() {
        super.initialize()

        recursiveHealthMapper = RecursiveMapper(
                world.getMapper(ParentOfCom::class.java),
                world.getMapper(HealthCom::class.java))
    }

    override fun processSystem() {}


    override fun inserted(entityId: Int) {
        bodyMapper[entityId].body.addCallbackToAll(collisionCallback)
    }

    private val collisionCallback = object: RCollisionCallback{
        override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val owner = ownerRFixture.ownerId
            val other = otherRFixture.ownerId

            val effect = pelletCollisionMapper[owner]

            if(other != -1){
                val health = recursiveHealthMapper.safeGet(other)
                if(health != null){
                    health.healthPoints -= effect.damage
                    if(effect.diesOnAttack){
                        world.delete(owner)
                    }
                }
            }

            if(!effect.diesOnAttack && effect.diesOnCollision){
                world.delete(owner)
            }
        }

        override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {}
        override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {}
        override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {}
    }

}
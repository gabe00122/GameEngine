package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold
import gabek.sm2.components.BodyCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.components.character.CharacterStateCom
import gabek.sm2.components.ParentOfCom
import gabek.sm2.factory.PelletFactory
import gabek.sm2.physics.RBody
import gabek.sm2.physics.RCollisionCallback
import gabek.sm2.physics.RFixture
import gabek.sm2.systems.FactoryManager

/**
 * @author Gabriel Keith
 */
class CharacterControllerSystem : BaseEntitySystem(
        Aspect.all(
                CharacterControllerCom::class.java,
                CharacterStateCom::class.java,
                BodyCom::class.java
        )) {
    private lateinit var parentMapper: ComponentMapper<ParentOfCom>
    private lateinit var characterControllerMapper: ComponentMapper<CharacterControllerCom>
    private lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val control = characterControllerMapper[entity]
            val state = characterStateMapper[entity]
            val body = bodyMapper[entity]

            val jumpTimeOut = 0.1f

            //damping
            if (!state.onGround) {
                val damp = -body.rBody.linearVelocityX * body.rBody.mass * 0.1f
                body.rBody.applyForceToCenter(damp, 0f, false)
                state.jumpTimeOut = jumpTimeOut
            }

            //count down timeout
            if (state.jumpTimeOut > 0 && state.onGround) {
                state.jumpTimeOut -= world.delta
            }

            val groundFixture = state.groundFixture
            if (control.moveUp && groundFixture != null && state.jumpTimeOut <= 0f) {
                jump(body.rBody, groundFixture.body!!, state.groundContactPoint)
                state.jumpTimeOut = jumpTimeOut
            }

            if (control.moveLeft) {
                state.lateralMovement = true
                state.direction = CharacterStateCom.Direction.LEFT
                state.legsMotor.motorSpeed = 360f * 2f

                if (!state.onGround && body.rBody.linearVelocityX > -4) {
                    body.rBody.applyForceToCenter(-200f * world.delta, 0f)
                }
            } else if (control.moveRight) {
                state.lateralMovement = true
                state.direction = CharacterStateCom.Direction.RIGHT
                state.legsMotor.motorSpeed = -360f * 2f

                if (!state.onGround && body.rBody.linearVelocityX < 4) {
                    body.rBody.applyForceToCenter(200f * world.delta, 0f)
                }
            } else {
                state.lateralMovement = false
                state.legsMotor.motorSpeed = 0f
            }
        }
    }

    private fun jump(body: RBody, groundBody: RBody, contactPoint: Vector2) {
        val power = 3f
        body.applyLinearImpulse(0f, power, body.x, body.y)
        groundBody.applyLinearImpulse(0f, -power, contactPoint.x, contactPoint.y)
    }

    override fun inserted(entityId: Int) {
        val state = characterStateMapper[entityId]

        bodyMapper[state.legChild].rBody.fixutres[0].callbackList.add(contactHandler)
    }

    private val contactHandler = object : RCollisionCallback {
        override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {}

        override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val state = characterStateMapper[parentMapper[ownerRFixture.ownerId].parent]
            val groundFixture = state.groundFixture

            if(groundFixture === otherRFixture){
                state.onGround = false
                state.groundFixture = null
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val body = ownerRFixture.body!!

            val state = characterStateMapper[parentMapper[ownerRFixture.ownerId].parent]
            val groundFixture = state.groundFixture

            val pad = 0.02f

            val diffX = contact.worldManifold.points[0].x - body.x
            if (diffX < 0.25f - pad && diffX > -0.25f + pad) {
                state.onGround = true
                state.groundFixture = otherRFixture
                state.groundContactPoint.set(contact.worldManifold.points[0])
            } else if (otherRFixture === groundFixture) {
                state.onGround = false
                state.groundFixture = null
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {}
    }
}
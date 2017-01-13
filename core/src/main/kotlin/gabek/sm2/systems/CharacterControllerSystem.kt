package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.*
import gabek.sm2.physics.RBody
import gabek.sm2.physics.RContact

/**
 * @author Gabriel Keith
 */
class CharacterControllerSystem : BaseEntitySystem(
        Aspect.all(
                CharacterControllerCom::class.java,
                CharacterPeripheryCom::class.java,
                CharacterStateCom::class.java,
                BodyCom::class.java,
                ContactCom::class.java
        )
) {
    private lateinit var characterControllerMapper: ComponentMapper<CharacterControllerCom>
    private lateinit var characterPeripheryMapper: ComponentMapper<CharacterPeripheryCom>
    private lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var contactMapper: ComponentMapper<ContactCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val control = characterControllerMapper[entity]
            val periphery = characterPeripheryMapper[entity]
            val state = characterStateMapper[entity]
            val body = bodyMapper[entity]
            val contact = contactMapper[entity]

            state.onGround = false

            var groundContact: RContact? = null
            for (c in contact.contacts) {
                if (c.body === periphery.body) {
                    val diffX = c.points[0].x - periphery.body.x
                    if(diffX < 0.24f && diffX > -0.24f) {
                        state.onGround = true
                        groundContact = c
                        break
                    }
                }
            }

            if (!state.onGround) {
                val damp = -body.rBody.linearVelocityX * body.rBody.mass * 0.1f
                body.rBody.applyForceToCenter(damp, 0f, false)
                state.jumpTimeOut = 0.05f
            }

            if (state.jumpTimeOut > 0 && state.onGround) {
                state.jumpTimeOut -= world.delta
            }

            if (control.moveUp && groundContact != null && state.jumpTimeOut <= 0f) {
                jump(body.rBody, groundContact)
                state.jumpTimeOut = 0.05f
            }
            if (control.moveLeft) {
                state.direction = CharacterStateCom.Direction.LEFT
                state.running = true

                if (state.onGround) {
                    periphery.motor.motorSpeed = 360f * 2f
                } else {
                    periphery.motor.motorSpeed = 0f
                    if (body.rBody.linearVelocityX > -4) {
                        body.rBody.applyForceToCenter(-200f * world.delta, 0f)
                    }
                }
            } else if (control.moveRight) {
                state.direction = CharacterStateCom.Direction.RIGHT
                state.running = true

                if (state.onGround) {
                    periphery.motor.motorSpeed = -360f * 2f
                } else {
                    periphery.motor.motorSpeed = 0f
                    if (body.rBody.linearVelocityX < 4) {
                        body.rBody.applyForceToCenter(200f * world.delta, 0f)
                    }
                }
            } else {
                state.running = false
                periphery.motor.motorSpeed = 0f
            }
        }
    }

    private fun jump(body: RBody, groundContact: RContact) {
        body.applyLinearImpulse(0f, 4f, body.x, body.y)
        groundContact.otherBody.applyLinearImpulse(0f, -4f, groundContact.points[0].x, groundContact.points[0].y)
    }
}
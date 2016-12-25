package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.*

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
            val id = entities[i]

            val control = characterControllerMapper[id]
            val legs = characterPeripheryMapper[id]
            val state = characterStateMapper[id]

            val body = bodyMapper[id]
            val contact = contactMapper[id]

            state.onGround = false
            for (c in contact.contacts) {
                if (c.body === legs.body) {
                    state.onGround = true
                    break
                }
            }

            if (!state.onGround) {
                val damp = -body.body.linearVelocityX * 0.2f
                body.body.applyForceToCenter(damp, 0f, false)
            }

            if (state.jumpTimeOut > 0) {
                state.jumpTimeOut -= world.delta
            }

            if (control.moveUp) {
                if (state.onGround && state.jumpTimeOut <= 0f && body.body.linearVelocityY > -0.1) {
                    state.jumpTimeOut = 0.2f

                    body.body.applyLinearInpulse(0f, 4f, body.body.x, body.body.y)
                }
            }
            if (control.moveLeft) {
                state.facingRight = false
                state.running = true

                if(state.onGround) {
                    legs.motor.motorSpeed = 360f * 2f
                } else {
                    legs.motor.motorSpeed = 0f
                    body.body.applyForceToCenter(-200f * world.delta, 0f)
                }
            } else if (control.moveRight) {
                state.facingRight = true
                state.running = true

                if(state.onGround) {
                    legs.motor.motorSpeed = -360f * 2f
                } else {
                    legs.motor.motorSpeed = 0f
                    body.body.applyForceToCenter(200f * world.delta, 0f)
                }
            } else {
                state.running = false
                legs.motor.motorSpeed = 0f
            }
        }
    }
}
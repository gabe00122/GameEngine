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

    private val listeners = mutableListOf<Listener>()

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
            for (c in contact.contacts) {
                if (c.body === periphery.body) {
                    state.onGround = true
                    break
                }
            }

            if (!state.onGround) {
                val damp = -body.rBody.linearVelocityX * body.rBody.mass * 0.1f
                body.rBody.applyForceToCenter(damp, 0f, false)
            }

            if (periphery.jumpTimeOut > 0) {
                periphery.jumpTimeOut -= world.delta
            }

            if (control.moveUp) {
                if (state.onGround && periphery.jumpTimeOut <= 0f && body.rBody.linearVelocityY > -0.1) {
                    jump(entity)
                }
            }
            if (control.moveLeft) {
                state.facingRight = false
                state.running = true

                if(state.onGround) {
                    periphery.motor.motorSpeed = 360f * 2f
                } else {
                    periphery.motor.motorSpeed = 0f
                    body.rBody.applyForceToCenter(-200f * world.delta, 0f)
                }
            } else if (control.moveRight) {
                state.facingRight = true
                state.running = true

                if(state.onGround) {
                    periphery.motor.motorSpeed = -360f * 2f
                } else {
                    periphery.motor.motorSpeed = 0f
                    body.rBody.applyForceToCenter(200f * world.delta, 0f)
                }
            } else {
                state.running = false
                periphery.motor.motorSpeed = 0f
            }
        }
    }

    private fun jump(entity: Int){
        characterPeripheryMapper[entity].jumpTimeOut = 0.2f

        val body = bodyMapper[entity].rBody
        body.applyLinearInpulse(0f, 4f, body.x, body.y)

        for(l in listeners){
            l.onJump(entity)
        }
    }

    fun addListener(listener: Listener){
        listeners.add(listener)
    }

    interface Listener {
        fun onJump(entity: Int)
    }
}
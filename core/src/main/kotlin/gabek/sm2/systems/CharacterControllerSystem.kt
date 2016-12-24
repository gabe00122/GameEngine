package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.physics.box2d.Contact
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
){
    private lateinit var characterControllerMapper: ComponentMapper<CharacterControllerCom>
    private lateinit var characterPeripheryMapper: ComponentMapper<CharacterPeripheryCom>
    private lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var contactMapper: ComponentMapper<ContactCom>

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val id = entities[i]

            val control = characterControllerMapper[id]
            val legs = characterPeripheryMapper[id]
            val state = characterStateMapper[id]

            val body = bodyMapper[id]
            val contact = contactMapper[id]

            state.onGround = false
            for(c in contact.contacts){
                if(c.body === legs.body){
                    state.onGround = true
                    break
                }
            }

            if(state.jumpTimeOut > 0){
                state.jumpTimeOut -= world.delta
            }

            if(control.moveUp){
                if(state.onGround && state.jumpTimeOut <= 0f && body.body.linearVelocityY > -0.1) {
                    state.jumpTimeOut = 0.2f

                    body.body.applyLinearInpulse(0f, 10f, body.body.x, body.body.y)
                }
            }
            if(control.moveLeft){
                state.facingRight = false
                state.running = true

                body.body.applyForceToCenter(-200f * world.delta, 0f)
                legs.motor.motorSpeed = 360f * 2f
            }
            else if(control.moveRight){
                state.facingRight = true
                state.running = true

                body.body.applyForceToCenter(200f * world.delta, 0f)
                legs.motor.motorSpeed = -360f * 2f
            }
            else{
                state.running = false

                legs.motor.motorSpeed = 0f
            }
        }
    }
}
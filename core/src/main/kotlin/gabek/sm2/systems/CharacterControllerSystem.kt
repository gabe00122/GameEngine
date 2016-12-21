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
                CharacterLegsCom::class.java,
                BodyCom::class.java
        )
){
    private lateinit var characterControllerMapper: ComponentMapper<CharacterControllerCom>
    private lateinit var characterLegsMapper: ComponentMapper<CharacterLegsCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var contactMapper: ComponentMapper<ContactCom>

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val id = entities[i]

            val control = characterControllerMapper[id]
            val legs = characterLegsMapper[id]
            val body = bodyMapper[id]
            val contact = contactMapper[id]

            if(control.moveUp){
                if(contact.contacts.size > 0) {
                    body.body.applyLinearInpulse(0f, 2.5f, body.body.x, body.body.y)
                }
            }
            if(control.moveLeft){
                body.body.applyForceToCenter(-200f * world.delta, 0f)
                legs.motor.motorSpeed = 360f * 2f
            }
            else if(control.moveRight){
                body.body.applyForceToCenter(200f * world.delta, 0f)
                legs.motor.motorSpeed = -360f * 2f
            }
            else{
                legs.motor.motorSpeed = 0f
            }
        }
    }
}
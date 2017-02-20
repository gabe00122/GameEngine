package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.utils.BitVector
import com.badlogic.gdx.Gdx
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.character.CharacterAnimatorCom
import gabek.sm2.components.character.CharacterMovementStateCom
import gabek.sm2.components.character.CharacterMovementStateCom.State
import gabek.sm2.components.character.CharacterMovementStateCom.State.*
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.systems.graphics.AnimationSystem

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorSystem : BaseEntitySystem(Aspect.all(
        AnimationCom::class.java, CharacterAnimatorCom::class.java
)) {

    private lateinit var animationMapper: ComponentMapper<AnimationCom>
    private lateinit var characterAnimatorMapper: ComponentMapper<CharacterAnimatorCom>

    private lateinit var animationSystem: AnimationSystem
    private lateinit var characterControllerSystem: CharacterControllerSystem

    override fun initialize() {
        super.initialize()

        val table = characterControllerSystem.transitionTable
        table.addListenerEntering(RUNNING){ entity, from, to ->
            if(isInterested(entity)) {
                val running = characterAnimatorMapper[entity].runningAnimationDef!!
                animationSystem.setAnimationDef(entity, running)
            }
        }

        table.addListenerEntering(STANDING){ entity, from, to ->
            if(isInterested(entity)) {
                val still = characterAnimatorMapper[entity].stillAnimationDef!!
                animationSystem.setAnimationDef(entity, still)
            }
        }

        table.addListenerEntering(JUMPING){ entity, from, to ->
            if(isInterested(entity)) {
                val jumping = characterAnimatorMapper[entity].jumpingAnimationDef!!
                animationSystem.setAnimationDef(entity, jumping)
            }
        }


    }

    fun isInterested(entity: Int): Boolean{
        return subscription.aspect.isInterested(world.getEntity(entity))
    }

    override fun processSystem() {}

    override fun inserted(entityId: Int) {
        super.inserted(entityId)
        animationMapper[entityId].currentAnimationDef = characterAnimatorMapper[entityId].stillAnimationDef
    }
}
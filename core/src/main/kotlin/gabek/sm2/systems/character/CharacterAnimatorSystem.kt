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
import gabek.sm2.graphics.AnimationDef
import gabek.sm2.systems.graphics.AnimationSystem

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorSystem : BaseEntitySystem(Aspect.all(
        AnimationCom::class.java, CharacterAnimatorCom::class.java
)) {
    private val members = BitVector()

    private lateinit var animationMapper: ComponentMapper<AnimationCom>
    private lateinit var characterAnimatorMapper: ComponentMapper<CharacterAnimatorCom>

    private lateinit var animationSystem: AnimationSystem
    private lateinit var characterControllerSystem: CharacterControllerSystem

    override fun initialize() {
        super.initialize()

        setAnimationFor(LANDING) {stillAnimationDef}
        setAnimationFor(STANDING) {stillAnimationDef}
        setAnimationFor(RUNNING) {runningAnimationDef}
        setAnimationFor(JUMPING) {jumpingAnimationDef}

        characterControllerSystem.transitionTable.addListener(RUNNING, IN_AIR){ entity, to, from ->
            if(isInterested(entity)) {
                animationSystem.setAnimationDef(entity, characterAnimatorMapper[entity].stillAnimationDef!!)
            }
        }
    }

    private inline fun setAnimationFor(entering: State, crossinline animationDef: CharacterAnimatorCom.() -> AnimationDef?){
        characterControllerSystem.transitionTable.addListenerEntering(entering) { entity, from, to ->
            if(isInterested(entity)){
                animationSystem.setAnimationDef(entity, animationDef(characterAnimatorMapper[entity])!!)
            }
        }
    }

    fun isInterested(entity: Int): Boolean{
        return members.get(entity)
    }

    override fun processSystem() {}

    override fun inserted(entityId: Int) {
        super.inserted(entityId)

        members.set(entityId)
        animationMapper[entityId].currentAnimationDef = characterAnimatorMapper[entityId].stillAnimationDef
    }

    override fun removed(entityId: Int) {
        super.removed(entityId)

        members.clear(entityId)
    }
}
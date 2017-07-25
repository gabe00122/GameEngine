package gabek.onebreath.system

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.utils.BitVector
import gabek.onebreath.component.CharacterAnimatorCom
import gabek.onebreath.component.CharacterMovementStateCom.State
import gabek.onebreath.component.CharacterMovementStateCom.State.*
import gabek.engine.core.components.graphics.AnimationCom
import gabek.engine.core.graphics.Animation
import gabek.engine.core.systems.graphics.AnimationSystem

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorSystem: BaseEntitySystem(Aspect.all(
        AnimationCom::class.java, CharacterAnimatorCom::class.java
)) {
    private val members = BitVector()

    private lateinit var animationMapper: ComponentMapper<AnimationCom>
    private lateinit var characterAnimatorMapper: ComponentMapper<CharacterAnimatorCom>

    private lateinit var animationSystem: AnimationSystem
    private lateinit var characterControllerSystem: CharacterControllerSystem

    override fun initialize() {
        super.initialize()

        setAnimationFor(LANDING) { stillAnimation }
        setAnimationFor(STANDING) { stillAnimation }
        setAnimationFor(RUNNING) { runningAnimation }
        setAnimationFor(JUMPING) { jumpingAnimation }

        characterControllerSystem.transitionTable.addListener(RUNNING, IN_AIR) { entity, to, from ->
            if (isInterested(entity)) {
                animationSystem.beginAnimation(entity, characterAnimatorMapper[entity].stillAnimation!!)
            }
        }
    }

    private inline fun setAnimationFor(entering: State, crossinline animation: CharacterAnimatorCom.() -> Animation?) {
        characterControllerSystem.transitionTable.addListenerEntering(entering) { entity, from, to ->
            if (isInterested(entity)) {
                val def = animation(characterAnimatorMapper[entity])
                if (def != null) {
                    animationSystem.beginAnimation(entity, def)
                }
            }
        }
    }

    fun isInterested(entity: Int): Boolean {
        return members.get(entity)
    }

    override fun processSystem() {}

    override fun inserted(entityId: Int) {
        super.inserted(entityId)

        members.set(entityId)
        animationSystem.beginAnimation(entityId, characterAnimatorMapper[entityId].stillAnimation!!)
    }

    override fun removed(entityId: Int) {
        super.removed(entityId)

        members.clear(entityId)
    }
}
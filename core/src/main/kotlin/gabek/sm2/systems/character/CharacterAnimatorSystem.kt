package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.character.CharacterAnimatorCom
import gabek.sm2.components.character.CharacterStateCom
import gabek.sm2.components.graphics.SpriteCom

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorSystem : BaseEntitySystem(Aspect.all(
        AnimationCom::class.java, SpriteCom::class.java, CharacterStateCom::class.java, CharacterAnimatorCom::class.java
)) {

    private lateinit var animationMapper: ComponentMapper<AnimationCom>
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    private lateinit var characterAnimatorMapper: ComponentMapper<CharacterAnimatorCom>

    override fun processSystem() {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val state = characterStateMapper[entity]
            val sprite = spriteMapper[entity]
            val animator = characterAnimatorMapper[entity]
            val animation = animationMapper[entity]

            sprite.flipX = state.direction == CharacterStateCom.Direction.RIGHT

            if (animation.currentAnimation == null) {
                animation.currentAnimation = animator.stillAnimation
            }

            if (state.onGround && state.lateralMovement && canSwitchRunning(animation, animator, state)) {
                animation.currentAnimation = animator.runningAnimation
                animation.reset()
            }

            if (state.onGround && !state.lateralMovement && canSwitchStill(animation, animator, state)) {
                animation.currentAnimation = animator.stillAnimation
                animation.reset()
            }

            if (!state.onGround && animation.currentAnimation === animator.runningAnimation) {
                animation.currentAnimation = animator.stillAnimation
                animation.reset()
            }

            if (!state.onGround && state.jumpTimeOut > 0 && animation.currentAnimation !== animator.jumpingAnimation) {
                animation.currentAnimation = animator.jumpingAnimation
                animation.reset()
            }
        }
    }

    private fun canSwitchRunning(animation: AnimationCom, animator: CharacterAnimatorCom, state: CharacterStateCom): Boolean {
        return animation.currentAnimation === animator.stillAnimation ||
                animation.currentAnimation === animator.jumpingAnimation && state.jumpTimeOut <= 0
    }

    private fun canSwitchStill(animation: AnimationCom, animator: CharacterAnimatorCom, state: CharacterStateCom): Boolean {
        return animation.currentAnimation === animator.runningAnimation ||
                animation.currentAnimation === animator.jumpingAnimation && state.jumpTimeOut <= 0
    }
}
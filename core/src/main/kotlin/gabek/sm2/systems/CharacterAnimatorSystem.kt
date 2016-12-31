package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.AnimationCom
import gabek.sm2.components.CharacterAnimatorCom
import gabek.sm2.components.CharacterStateCom
import gabek.sm2.components.SpriteCom

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorSystem : BaseEntitySystem(Aspect.all(
        AnimationCom::class.java, SpriteCom::class.java, CharacterStateCom::class.java, CharacterAnimatorCom::class.java
        )){

    private lateinit var animationMapper: ComponentMapper<AnimationCom>
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    private lateinit var characterAnimatorMapper: ComponentMapper<CharacterAnimatorCom>

    override fun processSystem() {
        val entities = entityIds
        for(i in 0 until entities.size()){
            val entity = entities[i]

            val state = characterStateMapper[entity]
            val sprite = spriteMapper[entity]
            val animator = characterAnimatorMapper[entity]
            val animation = animationMapper[entity]

            sprite.flipX = state.facingRight

            if(animation.currentAnimation == null){
                animation.currentAnimation = animator.stillAnimation
            }

            if(state.onGround && state.running && canSwitchRunning(animation, animator)) {
                animation.currentAnimation = animator.runningAnimation
                animation.reset()
            }

            if(state.onGround && !state.running && canSwitchStill(animation, animator)){
                animation.currentAnimation = animator.stillAnimation
                animation.reset()
            }

            if(!state.onGround && animation.currentAnimation == animator.runningAnimation){
                animation.currentAnimation = animator.stillAnimation
                animation.reset()
            }
        }
    }

    private fun canSwitchRunning(animation: AnimationCom, animator: CharacterAnimatorCom): Boolean {
        return animation.currentAnimation == animator.stillAnimation ||
                animation.currentAnimation == animator.jumpingAnimation && animation.isFinished
    }

    private fun canSwitchStill(animation: AnimationCom, animator: CharacterAnimatorCom): Boolean {
        return animation.currentAnimation == animator.runningAnimation ||
                animation.currentAnimation == animator.jumpingAnimation && animation.isFinished
    }
}
package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.AnimationCom
import gabek.sm2.components.SpriteCom

/**
 * @author Gabriel Keith
 */
class AnimationSystem : BaseEntitySystem(Aspect.all(SpriteCom::class.java, AnimationCom::class.java)) {
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var animationMapper: ComponentMapper<AnimationCom>

    override fun processSystem() {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val sprite = spriteMapper[entity]
            val animation = animationMapper[entity]

            val ref = animation.currentAnimation

            if (ref != null && ref.frames.isNotEmpty()) {
                if (animation.isStart) {
                    sprite.texture = ref.frames[animation.frame]
                    animation.isStart = false
                }

                if (!animation.isFinished) {
                    animation.clock += world.delta
                    if (animation.clock >= ref.delay) {
                        animation.clock -= ref.delay

                        //loop back to start
                        if (!ref.pingpong) {
                            if (!animation.isReverse && animation.frame >= ref.frames.size - 1) {
                                animation.frame = -1
                            }
                            if (animation.isReverse && animation.frame <= 0) {
                                animation.frame = ref.frames.size
                            }
                        }

                        //increment
                        if (animation.isReverse) animation.frame-- else animation.frame++

                        if (!animation.isReverse && animation.frame >= ref.frames.size - 1 ||
                                animation.isReverse && animation.frame <= 0) {
                            //switch direction
                            if (ref.pingpong) {
                                animation.isReverse = !animation.isReverse
                            }
                            //finish
                            if (!ref.repeats) {
                                animation.isFinished = true
                            }
                        }

                        sprite.texture = ref.frames[animation.frame]
                    }
                }
            }
        }
    }

}
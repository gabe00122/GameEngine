package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.AnimationCom
import gabek.sm2.components.SpriteCom

/**
 * @author Gabriel Keith
 */
class AnimationSystem : BaseEntitySystem(Aspect.all(SpriteCom::class.java, AnimationCom::class.java)){
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var animationMapper: ComponentMapper<AnimationCom>

    override fun processSystem() {
        val entities = entityIds
        for(i in 0 until entities.size()){
            val entity = entities[i]

            val sprite = spriteMapper[entity]
            val animation = animationMapper[entity]

            val ref = animation.animation

            if(ref != null && ref.frames.isNotEmpty()) {
                if (animation.isStart) {
                    sprite.texture = ref.frames[animation.frame]
                    animation.isStart = false
                }

                if (ref.delay > 0) {
                    animation.clock += world.delta
                    if (animation.clock >= ref.delay) {
                        animation.clock -= ref.delay

                        animation.frame += if (animation.isReverse) -1 else 1
                        if (animation.frame >= ref.frames.size) {
                            animation.frame = 0
                        }
                        if (animation.frame < 0) {
                            animation.frame = ref.frames.size - 1
                        }

                        if (ref.pingpong && (animation.frame == ref.frames.size - 1 || animation.frame == 0)) {
                            animation.isReverse = !animation.isReverse
                        }

                        sprite.texture = ref.frames[animation.frame]
                    }
                }
            }
        }
    }

}
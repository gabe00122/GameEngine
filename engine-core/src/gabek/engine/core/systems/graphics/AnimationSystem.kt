package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.graphics.AnimationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.graphics.AnimationDef

/**
 * @author Gabriel Keith
 */
class AnimationSystem : BaseEntitySystem(Aspect.all(SpriteCom::class.java, AnimationCom::class.java)) {
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var animationMapper: ComponentMapper<AnimationCom>

    override fun processSystem() {
        //TODO replace me, sooo ugly

        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val sprite = spriteMapper[entity]
            val animation = animationMapper[entity]

            val ref = animation.currentAnimationDef

            if (ref != null && ref.frames.isNotEmpty()) {
                if (animation.isStart) {
                    sprite.textureRef = ref.frames[animation.frame]
                    animation.isStart = false
                }

                if (!animation.isFinished) {
                    animation.clock += world.delta
                    if (animation.clock >= ref.delay) {
                        animation.clock -= ref.delay

                        //loop back to start
                        if (ref.strategy != AnimationDef.Strategy.PINGPONG) {
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
                            if (ref.strategy == AnimationDef.Strategy.PINGPONG) {
                                animation.isReverse = !animation.isReverse
                            }
                            //finish
                            if (ref.strategy != AnimationDef.Strategy.PINGPONG) {
                                animation.isFinished = true
                            }
                        }

                        sprite.textureRef = ref.frames[animation.frame]
                    }
                }
            }
        }
    }

    fun setAnimationDef(entity: Int, animationDef: AnimationDef) {
        val def = animationMapper[entity]
        def.currentAnimationDef = animationDef

        def.clock = 0f
        def.frame = 0
        def.isReverse = false
        def.isStart = true
        def.isFinished = false
    }
}
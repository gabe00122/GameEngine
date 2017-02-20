package gabek.sm2.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion
import gabek.sm2.Assets

/**
 * @author Gabriel Keith
 */
class AnimationDef
private constructor(
        val delay: Float,
        val strategy: Strategy,
        val frames: List<TextureRef>
) {

    companion object{
        fun builder(assets: Assets): Builder = Builder(assets)

        fun build(assets: Assets, buildFun: Builder.() -> Unit): AnimationDef{
            val builder = Builder(assets)
            builder.buildFun()
            return builder.build()
        }
    }
    
    class Builder(val assets: Assets){

        var delay = 0f
        var strategy: Strategy = Strategy.SINGLE
        private var frames: MutableList<TextureRef> = mutableListOf()

        fun setDelay(delay: Float): Builder{
            this.delay = delay
            return this
        }

        fun setStrategy(strategy: Strategy): Builder{
            this.strategy = strategy
            return this
        }

        fun addFrame(lookup: String): Builder{
            frames.add(assets.findTexture(lookup))
            return this
        }

        fun addFrames(lookup: String, range: IntRange): Builder{
            frames.addAll(assets.findTextures(lookup, range))
            return this
        }

        fun build(): AnimationDef = AnimationDef(delay, strategy, frames)
    }
    
    enum class Strategy {
        SINGLE, REPEAT, PINGPONG
    }
}
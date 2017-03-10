package gabek.sm2.graphics

import gabek.sm2.assets.Assets

/**
 * @author Gabriel Keith
 */
class AnimationDef
private constructor(
        val delay: Float,
        val strategy: Strategy,
        val frames: List<TextureRef>
) {

    companion object {
        fun builder(assets: Assets): Builder = Builder(assets)

        fun build(assets: Assets, buildFun: Builder.() -> Unit): AnimationDef {
            val builder = Builder(assets)
            builder.buildFun()
            return builder.build()
        }
    }

    class Builder(val assets: Assets) {
        private var dirty = false

        var delay = 0f
            set(value) {
                if (dirty) reset()
                field = value
            }

        var strategy: Strategy = Strategy.SINGLE
            set(value) {
                if (dirty) reset()
                field = value
            }

        private var frames: MutableList<TextureRef> = mutableListOf()

        fun setDelay(delay: Float): Builder {
            this.delay = delay
            return this
        }

        fun setStrategy(strategy: Strategy): Builder {
            this.strategy = strategy
            return this
        }

        fun addFrame(lookup: String): Builder {
            if (dirty) reset()

            if (lookup.endsWith(":*")) {
                frames.addAll(assets.findTextures(lookup))
            } else {
                frames.add(assets.findTexture(lookup))
            }
            return this
        }

        fun addFrames(lookup: String, range: IntRange): Builder {
            if (dirty) reset()

            frames.addAll(assets.findTextures(lookup, range))
            return this
        }

        private fun reset() {
            dirty = false

            delay = 0f
            strategy = Strategy.SINGLE
            frames = mutableListOf()
        }

        fun build(): AnimationDef {
            dirty = true
            return AnimationDef(delay, strategy, frames)
        }
    }

    enum class Strategy {
        SINGLE, REPEAT, PINGPONG
    }
}
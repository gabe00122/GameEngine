package gabek.engine.core.graphics

import gabek.engine.core.assets.Assets

/**
 * @author Gabriel Keith
 * @date 7/25/2017
 */
class AnimationBuilder(val assets: Assets) {
    private var dirty = false

    var delay = 0f
        set(value) {
            if (dirty) reset()
            field = value
        }

    var strategy = Animation.Strategy.SINGLE
        set(value) {
            if (dirty) reset()
            field = value
        }

    private var frames: MutableList<Sprite> = mutableListOf()

    fun setDelay(delay: Float): AnimationBuilder {
        this.delay = delay
        return this
    }

    fun setStrategy(strategy: Animation.Strategy): AnimationBuilder {
        this.strategy = strategy
        return this
    }

    fun addFrame(lookup: String): AnimationBuilder {
        if (dirty) reset()

        if (lookup.endsWith(":*")) {
            frames.addAll(assets.getTextures(lookup))
        } else {
            frames.add(assets.getTexture(lookup))
        }
        return this
    }

    fun addFrames(lookup: String, range: IntRange): AnimationBuilder {
        if (dirty) reset()

        frames.addAll(assets.getTextures(lookup, range))
        return this
    }

    private fun reset() {
        dirty = false

        delay = 0f
        strategy = Animation.Strategy.SINGLE
        frames = mutableListOf()
    }

    fun build(): Animation {
        dirty = true
        return Animation(delay, strategy, frames)
    }
}
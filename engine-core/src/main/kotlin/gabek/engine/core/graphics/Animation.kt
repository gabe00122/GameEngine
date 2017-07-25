package gabek.engine.core.graphics

import gabek.engine.core.assets.Assets

/**
 * @author Gabriel Keith
 */
class Animation
internal constructor(
        val delay: Float,
        val strategy: Strategy,
        val frames: List<Sprite>
) {


    fun getSprite(time: Float): Sprite {
        when(strategy){
            Strategy.SINGLE -> {
                return frames[0]
            }
            Strategy.REPEAT -> {
                return frames[(time / delay).toInt().rem(frames.size)]
            }
            Strategy.PINGPONG -> {
                val raw = (time / delay).toInt()
                val even = (raw / frames.size).rem(2) == 0
                return if(even) frames[raw.rem(frames.size)] else frames[frames.size - raw.rem(frames.size) - 1]
            }
        }
    }


    companion object {
        fun builder(assets: Assets) = AnimationBuilder(assets)

        fun build(assets: Assets, buildFun: AnimationBuilder.() -> Unit): Animation {
            val builder = AnimationBuilder(assets)
            builder.buildFun()
            return builder.build()
        }
    }

    enum class Strategy {
        SINGLE, REPEAT, PINGPONG
    }
}
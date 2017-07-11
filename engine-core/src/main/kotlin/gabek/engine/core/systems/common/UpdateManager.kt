package gabek.engine.core.systems.common

import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import gabek.engine.core.graphics.Profiler

/**
 * @author Gabriel Keith
 */
class UpdateManager : BaseSystem() {
    var frequency = 60f

    var currentFrame = 0
        private set

    val step: Float = 1f / frequency
    var accumulator: Float = 0f
    val progress: Float
        get() = accumulator / step

    override fun initialize() {
        super.initialize()
        world.delta = step
    }

    fun update(delta: Float) {
        accumulator += delta

        while(accumulator >= step) {
            world.process()
            accumulator -= step
        }
    }

    override fun processSystem() {
        currentTime = ++currentFrame * world.delta
    }

    fun getElapsedFrames(otherFrame: Int): Int = currentFrame - otherFrame
    fun getElapsedTime(otherFrame: Int): Float = (currentFrame - otherFrame) * world.delta

    var currentTime: Float = 0f
        private set
}
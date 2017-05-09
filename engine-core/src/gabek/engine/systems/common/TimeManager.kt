package gabek.engine.systems.common

import com.artemis.BaseSystem

/**
 * @author Gabriel Keith
 */
class TimeManager : BaseSystem() {
    var currentFrame = 0
        private set

    override fun processSystem() {
        currentFrame++
    }

    fun getElapsedFrames(otherFrame: Int): Int = currentFrame - otherFrame
    fun getElapsedTime(otherFrame: Int): Float = (currentFrame - otherFrame) * world.delta
}
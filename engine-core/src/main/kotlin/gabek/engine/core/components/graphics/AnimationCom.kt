package gabek.engine.core.components.graphics

import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.AnimationRef

/**
 * @author Gabriel Keith
 */
class AnimationCom : RComponent<AnimationCom>() {
    var currentAnimationRef: AnimationRef? = null
    var clock: Float = 0f
    var frame: Int = 0
    var isReverse = false
    var isStart = true
    var isFinished = false

    override fun set(other: AnimationCom) {
        currentAnimationRef = other.currentAnimationRef
        clock = other.clock
        frame = other.frame
        isReverse = other.isReverse
        isStart = other.isStart
        isFinished = other.isFinished
    }

    override fun reset() {
        currentAnimationRef = null
        clock = 0f
        frame = 0
        isReverse = false
        isStart = true
        isFinished = false
    }

    override fun toString() = "AnimationCom: currentAnimationRef = $currentAnimationRef, clock = $clock, frame = $frame, " +
            "isReverse = $isReverse, isStart = $isStart, isFinished = $isFinished"
}
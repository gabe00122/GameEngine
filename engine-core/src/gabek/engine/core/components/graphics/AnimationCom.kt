package gabek.engine.core.components.graphics

import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.AnimationDef

/**
 * @author Gabriel Keith
 */
class AnimationCom : RComponent<AnimationCom>() {
    var currentAnimationDef: AnimationDef? = null
    var clock: Float = 0f
    var frame: Int = 0
    var isReverse = false
    var isStart = true
    var isFinished = false

    override fun set(other: AnimationCom) {
        currentAnimationDef = other.currentAnimationDef
        clock = other.clock
        frame = other.frame
        isReverse = other.isReverse
        isStart = other.isStart
        isFinished = other.isFinished
    }

    override fun reset() {
        currentAnimationDef = null
        clock = 0f
        frame = 0
        isReverse = false
        isStart = true
        isFinished = false
    }

    override fun toString() = "AnimationCom: currentAnimationDef = $currentAnimationDef, clock = $clock, frame = $frame, " +
            "isReverse = $isReverse, isStart = $isStart, isFinished = $isFinished"
}
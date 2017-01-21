package gabek.sm2.components.graphics

import com.artemis.Component
import gabek.sm2.graphics.Animation

/**
 * @author Gabriel Keith
 */
class AnimationCom : Component() {
    var currentAnimation: Animation? = null
    var clock: Float = 0f
    var frame: Int = 0
    var isReverse = false
    var isStart = true
    var isFinished = false

    fun reset() {
        clock = 0f
        frame = 0
        isReverse = false
        isStart = true
        isFinished = false
    }
}
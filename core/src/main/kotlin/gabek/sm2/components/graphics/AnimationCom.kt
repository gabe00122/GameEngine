package gabek.sm2.components.graphics

import com.artemis.Component
import gabek.sm2.graphics.AnimationDef

/**
 * @author Gabriel Keith
 */
class AnimationCom : Component() {
    var currentAnimationDef: AnimationDef? = null
    var clock: Float = 0f
    var frame: Int = 0
    var isReverse = false
    var isStart = true
    var isFinished = false
}
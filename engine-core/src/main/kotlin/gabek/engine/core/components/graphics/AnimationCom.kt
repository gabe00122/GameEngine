package gabek.engine.core.components.graphics

import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.Animation

/**
 * @author Gabriel Keith
 */
class AnimationCom : RComponent<AnimationCom>() {
    var animation: Animation? = null
    var startFrame: Int = 0

    override fun set(other: AnimationCom) {
        animation = other.animation
        startFrame = other.startFrame
    }

    override fun reset() {
        animation = null
        startFrame = 0
    }
}
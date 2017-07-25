package gabek.onebreath.component

import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.Animation

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorCom : RComponent<CharacterAnimatorCom>() {
    var runningAnimation: Animation? = null
    var stillAnimation: Animation? = null
    var jumpingAnimation: Animation? = null

    override fun set(other: CharacterAnimatorCom) {
        runningAnimation = other.runningAnimation
        stillAnimation = other.stillAnimation
        jumpingAnimation = other.jumpingAnimation
    }

    override fun reset() {
        runningAnimation = null
        stillAnimation = null
        jumpingAnimation = null
    }

    override fun toString() = "CharacterAnimatorCom"
}
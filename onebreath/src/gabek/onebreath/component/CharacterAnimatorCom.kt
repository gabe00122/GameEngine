package gabek.onebreath.component

import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.AnimationRef

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorCom : RComponent<CharacterAnimatorCom>() {
    var runningAnimationRef: AnimationRef? = null
    var stillAnimationRef: AnimationRef? = null
    var jumpingAnimationRef: AnimationRef? = null

    override fun set(other: CharacterAnimatorCom) {
        runningAnimationRef = other.runningAnimationRef
        stillAnimationRef = other.stillAnimationRef
        jumpingAnimationRef = other.jumpingAnimationRef
    }

    override fun reset() {
        runningAnimationRef = null
        stillAnimationRef = null
        jumpingAnimationRef = null
    }

    override fun toString() = "CharacterAnimatorCom"
}
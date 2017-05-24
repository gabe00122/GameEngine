package gabek.engine.core.components.character

import gabek.engine.core.components.RComponent
import gabek.engine.core.graphics.AnimationDef

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorCom : RComponent<CharacterAnimatorCom>() {
    var runningAnimationDef: AnimationDef? = null
    var stillAnimationDef: AnimationDef? = null
    var jumpingAnimationDef: AnimationDef? = null

    override fun set(other: CharacterAnimatorCom) {
        runningAnimationDef = other.runningAnimationDef
        stillAnimationDef = other.stillAnimationDef
        jumpingAnimationDef = other.jumpingAnimationDef
    }

    override fun reset() {
        runningAnimationDef = null
        stillAnimationDef = null
        jumpingAnimationDef = null
    }

    override fun toString() = "CharacterAnimatorCom"
}
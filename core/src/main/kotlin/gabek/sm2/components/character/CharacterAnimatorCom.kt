package gabek.sm2.components.character

import com.artemis.Component
import gabek.sm2.graphics.AnimationDef

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorCom : Component() {
    var runningAnimationDef: AnimationDef? = null
    var stillAnimationDef: AnimationDef? = null
    var jumpingAnimationDef: AnimationDef? = null
}
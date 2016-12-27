package gabek.sm2.components

import com.artemis.Component
import gabek.sm2.graphics.Animation

/**
 * @author Gabriel Keith
 */
class CharacterAnimatorCom : Component() {
    var runningAnimation: Animation? = null
    var stillAnimation: Animation? = null
    var jumpingAnimation: Animation? = null
}
package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import gabek.sm2.graphics.Animation

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class CharacterAnimatorCom : Component() {
    @JvmField var runningAnimation: Animation? = null
    @JvmField var stillAnimation: Animation? = null
    @JvmField var jumpingAnimation: Animation? = null
}
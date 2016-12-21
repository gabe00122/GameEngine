package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.DelayedComponentRemoval
import gabek.sm2.physics.RBody

/**
 * @author Gabriel Keith
 */
@DelayedComponentRemoval
class BodyCom : Component(){
    var body = RBody()
}
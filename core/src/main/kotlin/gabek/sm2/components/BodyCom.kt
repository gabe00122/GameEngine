package gabek.sm2.components

import com.artemis.annotations.DelayedComponentRemoval
import gabek.sm2.physics.RBody

/**
 * @author Gabriel Keith
 */
@DelayedComponentRemoval
class BodyCom: RComponent<BodyCom>() {
    var body = RBody()

    override fun set(other: BodyCom) {
        body.set(other.body)
    }

    override fun reset() {
        body.reset()
    }
}
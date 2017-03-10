package gabek.sm2.components.brains

import com.artemis.PooledComponent

/**
 * @author Gabriel Keith
 */
class WanderingBrainCom : PooledComponent() {
    var timeToTurn = 0

    override fun reset() {
        timeToTurn = 0
    }
}
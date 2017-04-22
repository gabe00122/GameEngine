package gabek.sm2.components.brains

import com.artemis.PooledComponent
import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 */
class WanderingBrainCom : RComponent<WanderingBrainCom>() {
    var timeToTurn = 0

    override fun set(other: WanderingBrainCom) {
        timeToTurn = other.timeToTurn
    }

    override fun reset() {
        timeToTurn = 0
    }

    override fun toString() = "WanderingBrainCom: timeToTurn = $timeToTurn"
}
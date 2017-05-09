package gabek.engine.components.brains

import gabek.engine.components.RComponent

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
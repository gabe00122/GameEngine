package gabek.onebreath.component

import gabek.engine.core.components.RComponent
import java.util.*

/**
 * @author Gabriel Keith
 */
class CharacterMovementStateCom : RComponent<CharacterMovementStateCom>() {
    companion object {
        val DEFAULT_STATE = State.STANDING

        val GROUND_STATES: EnumSet<State> = EnumSet.of(State.LANDING, State.STANDING, State.RUNNING)
    }

    var state: State = DEFAULT_STATE
    var timeOnGround = 0
    var timeInAir = 0

    override fun set(other: CharacterMovementStateCom) {
        state = other.state
        timeOnGround = other.timeOnGround
        timeInAir = other.timeInAir
    }

    override fun reset() {
        state = DEFAULT_STATE
        timeOnGround = 0
        timeInAir = 0
    }

    override fun toString() = "CharacterMovementStateCom: state = $state, airTime = $timeInAir, groundTime = $timeOnGround"

    enum class State {
        //STARTING,

        LANDING,
        STANDING,
        RUNNING,

        JUMPING,
        IN_AIR;
    }
}
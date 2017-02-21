package gabek.sm2.components.character

import com.artemis.Component
import com.artemis.PooledComponent
import java.util.*

/**
 * @author Gabriel Keith
 */
class CharacterMovementStateCom : Component() {
    companion object{
        val DEFAULT_STATE = State.STANDING

        val GROUND_STATES = EnumSet.of(State.LANDING, State.STANDING, State.RUNNING)
    }

    var state: State = DEFAULT_STATE
    var timeOnGround = 0
    var timeInAir = 0

    //override fun reset() {
    //    state = DEFAULT_STATE
    //    timeOnGround = 0
    //    timeInAir = 0
    //}

    enum class State {
        //STARTING,

        LANDING,
        STANDING,
        RUNNING,

        JUMPING,
        IN_AIR;
    }
}
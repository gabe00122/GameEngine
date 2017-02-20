package gabek.sm2.components.character

import com.artemis.PooledComponent

/**
 * @author Gabriel Keith
 */
class CharacterMovementStateCom : PooledComponent() {
    companion object{
        val DEFAULT_STATE = State.STANDING
    }

    var state: State = DEFAULT_STATE
    var timeInState = 0

    override fun reset() {
        state = DEFAULT_STATE
        timeInState = 0
    }

    enum class State {
        LANDING,
        STANDING,
        TIPTOE,
        DUCK,

        RUNNING,
        TIPTOE_RUN,
        CROUCH_WALK,

        JUMPING,
        IN_AIR,
        IN_AIR_MOVE
    }
}
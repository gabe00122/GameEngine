package gabek.sm2.components.character

import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 */
class BiDirectionCom : RComponent<BiDirectionCom>() {
    companion object {
        val DEFAULT_DIRECTION = Direction.LEFT
    }

    var direction: Direction = DEFAULT_DIRECTION
    var timeInState = 0

    override fun set(other: BiDirectionCom) {
        direction = other.direction
        timeInState = other.timeInState
    }

    override fun reset() {
        direction = DEFAULT_DIRECTION
        timeInState = 0
    }

    override fun toString() = "BiDirectionCom: direction = $direction, timeInState = $timeInState"

    enum class Direction {
        LEFT, RIGHT
    }
}
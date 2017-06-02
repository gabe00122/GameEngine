package gabek.onebreath.component

import gabek.engine.core.components.RComponent


/**
 * @author Gabriel Keith
 */
class BiDirectionCom : RComponent<BiDirectionCom>() {
    companion object {
        val DEFAULT_DIRECTION = Direction.LEFT
    }

    var direction: Direction = DEFAULT_DIRECTION
    var invert: Boolean = false
    var timeInState = 0

    override fun set(other: BiDirectionCom) {
        direction = other.direction
        invert = other.invert
        timeInState = other.timeInState
    }

    override fun reset() {
        direction = DEFAULT_DIRECTION
        invert = false
        timeInState = 0
    }

    override fun toString() = "BiDirectionCom: direction = $direction, invert = $invert, timeInState = $timeInState"

    enum class Direction {
        LEFT, RIGHT
    }
}
package gabek.sm2.components.character

import com.artemis.Component

/**
 * @author Gabriel Keith
 */
class BiDirectionCom : Component() {
    companion object {
        val DEFAULT_DIRECTION = Direction.LEFT
    }

    var direction: Direction = DEFAULT_DIRECTION
    var timeInState = 0

    //override fun reset() {
    //    direction = DEFAULT_DIRECTION
    //    timeInState = 0
    //}

    enum class Direction {
        LEFT, RIGHT
    }
}
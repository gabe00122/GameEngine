package gabek.sm2.components

import com.artemis.Component

/**
 * @author Gabriel Keith
 */
class CharacterStateCom : Component() {
    var direction: Direction = Direction.RIGHT
    var running = false
    var onGround = false

    var jumpTimeOut: Float = 0f

    enum class Direction {
        LEFT, RIGHT
    }
}
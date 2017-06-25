package gabek.onebreath.component

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */
class CharacterControllerCom : RComponent<CharacterControllerCom>() {
    var moveUp: Boolean = false
    var moveDown: Boolean = false
    var lateralMovement: Float = 0f

    var moveLeft: Boolean
        get() = lateralMovement < 0f
        set(value) {
            lateralMovement = if (value) -1f else 0f
        }

    var moveRight: Boolean
        get() = lateralMovement > 0f
        set(value) {
            lateralMovement = if (value) 1f else 0f
        }

    var jump: Boolean = false

    var primary: Boolean = false

    override fun set(other: CharacterControllerCom) {
        moveUp = other.moveUp
        moveDown = other.moveDown
        moveLeft = other.moveLeft
        moveRight = other.moveRight
        lateralMovement = other.lateralMovement

        jump = other.jump
        primary = other.primary
    }

    override fun reset() {
        moveUp = false
        moveDown = false
        moveLeft = false
        moveRight = false
        lateralMovement = 0f

        jump = false
        primary = false
    }

    override fun toString() = "CharacterControllerCom"
}
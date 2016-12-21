package gabek.sm2.input

/**
 * @author Gabriel Keith
 */
abstract class PlayerInput {
    abstract fun pollAction(actionId: Int): Boolean
    abstract fun update(delta: Float)

    object Actions{
        val SIZE = 5

        val UP = 0
        val DOWN = 1
        val LEFT = 2
        val RIGHT = 3
        val SELECT = 4
    }
}
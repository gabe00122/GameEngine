package gabek.sm2.input

/**
 * @author Gabriel Keith
 */
abstract class PlayerInput {
    abstract fun pollAction(actionId: Int): Boolean
    abstract fun update(delta: Float)
}
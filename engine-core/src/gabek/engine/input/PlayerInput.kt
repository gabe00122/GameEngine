package gabek.engine.input

/**
 * @author Gabriel Keith
 */
abstract class PlayerInput {
    abstract fun pollAction(actionId: Int): Boolean
}
package gabek.sm2.input


/**
 * @author Gabriel Keith
 */
class KeyboardPlayerInput : PlayerInput() {
    private val size = Actions.SIZE

    private val currentState = BooleanArray(size)

    override fun pollAction(actionId: Int): Boolean {
        return currentState[actionId]
    }

    fun keyUp(actionId: Int) {
        currentState[actionId] = false
    }

    fun keyDown(actionId: Int) {
        currentState[actionId] = true
    }
}
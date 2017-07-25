package gabek.engine.core.input


/**
 * @author Gabriel Keith
 */
class KeyboardPlayerInput internal constructor(
        private val inputManager: InputManager ,
        val id: Int
) : PlayerInput() {

    private val currentState = ArrayList<Boolean>()

    override fun pollAction(actionId: Int): Boolean {
        return actionId < currentState.size && currentState[actionId]
    }

    internal fun keyUp(actionId: Int) {
        ensureCapacity(actionId)
        currentState[actionId] = false
    }

    internal fun keyDown(actionId: Int) {
        ensureCapacity(actionId)
        currentState[actionId] = true
    }

    fun bindKey(actionCode: Int, keyCode: Int) {
        inputManager.bindKey(this, actionCode, keyCode)
    }

    private fun ensureCapacity(actionId: Int){
        while(actionId >= currentState.size) {
            currentState.add(false)
        }
    }
}
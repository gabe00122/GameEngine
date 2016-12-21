package gabek.sm2.input


/**
 * @author Gabriel Keith
 */
class KeyboardPlayerInput : PlayerInput(){
    private val size = Actions.SIZE

    private val currentState = BooleanArray(size)
    private val keysUp = BooleanArray(size)
    private val keysDown = BooleanArray(size)

    override fun update(delta: Float) {
        keysUp.fill(false)
        keysDown.fill(false)
    }

    override fun pollAction(actionId: Int): Boolean {
        return currentState[actionId] || keysDown[actionId]
    }

    fun keyUp(actionId: Int){
        keysUp[actionId] = true
        currentState[actionId] = false
    }

    fun keyDown(actionId: Int){
        keysDown[actionId] = true
        currentState[actionId] = true
    }
}
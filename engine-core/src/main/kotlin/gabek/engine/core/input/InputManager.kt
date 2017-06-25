package gabek.engine.core.input

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter

/**
 * @author Gabriel Keith
 */
class InputManager: PlayerInput() {
    private val inputHandler = InputHandler()
    val inputProcessor: InputProcessor get() = inputHandler

    private val keyBindings = Array<KeyBinding?>(255, { null })
    private val playerInputs = ArrayList<PlayerInput>()

    override fun pollAction(actionId: Int): Boolean = pollAllInputs(actionId) != null

    fun pollAllInputs(actionId: Int): PlayerInput? {
        return playerInputs.firstOrNull { it.pollAction(actionId) }
    }

    val playerInputSize: Int get() = playerInputs.size

    fun getPlayerInput(playerInputId: Int): PlayerInput {
        return playerInputs[playerInputId]
    }

    fun createKeyboardInput(): KeyboardPlayerInput{
        val keyboardInput = KeyboardPlayerInput(this, playerInputs.size)
        playerInputs.add(keyboardInput)
        return keyboardInput
    }

    internal fun bindKey(playerInput: KeyboardPlayerInput, actionId: Int, keycode: Int) {
        keyBindings[keycode] = KeyBinding(playerInput, actionId)
    }

    private data class KeyBinding(val playerInput: KeyboardPlayerInput, val actionId: Int)

    private inner class InputHandler: ControllerAdapter(), InputProcessor {
        override fun keyUp(keycode: Int): Boolean {
            val binding = keyBindings[keycode]
            if (binding != null) {
                binding.playerInput.keyUp(binding.actionId)
                return true
            }

            return false
        }

        override fun keyDown(keycode: Int): Boolean {
            val binding = keyBindings[keycode]
            if (binding != null) {
                binding.playerInput.keyDown(binding.actionId)
                return true
            }

            return false
        }

        override fun connected(controller: Controller) {
            //val input = ControllerPlayerInput()
            //controller.addListener(input)
            //playerInputs.add(input)
        }

        override fun disconnected(controller: Controller) {

        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
        override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
        override fun keyTyped(character: Char): Boolean = false
        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
        override fun scrolled(amount: Int): Boolean = false
        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    }
}
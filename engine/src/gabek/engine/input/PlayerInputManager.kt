package gabek.sm2.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter
import com.badlogic.gdx.controllers.Controllers
import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 */
class PlayerInputManager(val kodein: Kodein) : PlayerInput() {
    private val inputHandler = InputHandler()
    val inputProcessor: InputProcessor get() = inputHandler

    private val keyBindings = Array<KeyBinding?>(255, { null })

    private val playerInputs = mutableListOf<PlayerInput>()

    init {
        playerInputs.add(KeyboardPlayerInput())
        playerInputs.add(KeyboardPlayerInput())

        bindKey(0, Actions.UP, Input.Keys.UP)
        bindKey(0, Actions.DOWN, Input.Keys.DOWN)
        bindKey(0, Actions.LEFT, Input.Keys.LEFT)
        bindKey(0, Actions.RIGHT, Input.Keys.RIGHT)
        bindKey(0, Actions.SELECT, Input.Keys.ENTER)
        bindKey(0, Actions.ESCAPE, Input.Keys.ESCAPE)

        bindKey(1, Actions.UP, Input.Keys.W)
        bindKey(1, Actions.DOWN, Input.Keys.S)
        bindKey(1, Actions.LEFT, Input.Keys.A)
        bindKey(1, Actions.RIGHT, Input.Keys.D)
        bindKey(1, Actions.SELECT, Input.Keys.SPACE)

        Controllers.addListener(inputHandler)
        for (controller in Controllers.getControllers()) {
            val pi = ControllerPlayerInput()
            controller.addListener(pi)
            playerInputs.add(pi)
        }
    }

    override fun pollAction(actionId: Int): Boolean = pollAllInputs(actionId) != null

    fun pollAllInputs(actionId: Int): PlayerInput? {

        return playerInputs.firstOrNull { it.pollAction(actionId) }
    }

    val playerInputSize: Int get() = playerInputs.size

    fun getPlayerInput(playerInputId: Int): PlayerInput {
        return playerInputs[playerInputId]
    }

    fun bindKey(playerInputId: Int, actionId: Int, keycode: Int) {
        val playerInput = playerInputs[playerInputId] as KeyboardPlayerInput

        keyBindings[keycode] = KeyBinding(playerInput, actionId)
    }

    private class KeyBinding(val playerInput: KeyboardPlayerInput, val actionId: Int)

    private inner class InputHandler : ControllerAdapter(), InputProcessor {
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
            //val playerInput = ControllerPlayerInput()
            //controller.addListener(playerInput)
            //playerInputs.add(playerInput)
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
package gabek.spacemonk

import com.badlogic.gdx.Input
import gabek.engine.core.input.Actions
import gabek.engine.core.input.InputManager

/**
 * @another Gabriel Keith
 * @date 5/17/2017.
 */

fun buildInputManager(): InputManager {
    val inputManager = InputManager()
    val keyboard = inputManager.createKeyboardInput()
    keyboard.bindKey(Actions.UP, Input.Keys.UP)
    keyboard.bindKey(Actions.DOWN, Input.Keys.DOWN)
    keyboard.bindKey(Actions.LEFT, Input.Keys.LEFT)
    keyboard.bindKey(Actions.RIGHT, Input.Keys.RIGHT)

    return inputManager
}
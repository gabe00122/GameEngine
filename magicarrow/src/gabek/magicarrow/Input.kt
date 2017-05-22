package gabek.magicarrow

import com.badlogic.gdx.Input
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.input.InputManager

/**
 * @another Gabriel Keith
 * @date 5/17/2017.
 */

object ActionCodes {
    const val UP = 0
    const val DOWN = 1
    const val LEFT = 2
    const val RIGHT = 3
}

fun buildInputManager(): InputManager{
    val inputManager = InputManager()
    val primaryInput = inputManager.createKeyboardInput()

    primaryInput.bindKey(ActionCodes.UP, Input.Keys.UP)
    primaryInput.bindKey(ActionCodes.DOWN, Input.Keys.DOWN)
    primaryInput.bindKey(ActionCodes.LEFT, Input.Keys.LEFT)
    primaryInput.bindKey(ActionCodes.RIGHT, Input.Keys.RIGHT)

    return inputManager
}
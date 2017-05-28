package gabek.engine.core.systems

import com.artemis.ComponentMapper
import gabek.engine.core.components.InputCom
import gabek.engine.core.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class InputSystem: PassiveSystem() {
    private lateinit var inputMapper: ComponentMapper<InputCom>

    fun setInput(entity: Int, playerInput: PlayerInput) {
        inputMapper[entity].input = playerInput
    }
}
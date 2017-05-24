package gabek.engine.core.components

import gabek.engine.core.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class InputCom : RComponent<InputCom>() {
    @Transient var input: PlayerInput? = null

    override fun set(other: InputCom) {
        input = other.input
    }

    override fun reset() {
        input = null
    }

    override fun toString() = "InputCom"
}
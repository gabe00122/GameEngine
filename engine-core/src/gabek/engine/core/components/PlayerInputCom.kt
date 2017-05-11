package gabek.engine.core.components

import gabek.engine.core.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class PlayerInputCom: RComponent<PlayerInputCom>() {
    @Transient var playerInput: PlayerInput? = null

    override fun set(other: PlayerInputCom) {
        playerInput = other.playerInput
    }

    override fun reset() {
        playerInput = null
    }

    override fun toString() = "PlayerInputCom"
}
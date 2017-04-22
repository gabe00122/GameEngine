package gabek.sm2.components

import gabek.sm2.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class PlayerInputCom : RComponent<PlayerInputCom>() {
    @Transient var playerInput: PlayerInput? = null

    override fun set(other: PlayerInputCom) {
        playerInput = other.playerInput
    }

    override fun reset() {
        playerInput = null
    }

    override fun toString() = "PlayerInputCom"
}
package gabek.sm2.components

import com.artemis.Component
import gabek.sm2.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class PlayerInputCom : Component() {
    @Transient var playerInput: PlayerInput? = null
}
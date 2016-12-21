package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import gabek.sm2.input.PlayerInput

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class PlayerInputCom : Component(){
    @JvmField @Transient var playerInput: PlayerInput? = null
}
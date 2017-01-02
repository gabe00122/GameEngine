package gabek.sm2.components

import com.artemis.Component

/**
 * @author Gabriel Keith
 */
class CharacterStateCom : Component() {
    var facingRight = false
    var running = false
    var onGround = false

    var jumpTimeOut: Float = 0f
}
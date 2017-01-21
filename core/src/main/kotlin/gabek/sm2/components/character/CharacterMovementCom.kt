package gabek.sm2.components.character

import com.artemis.Component

/**
 * @author Gabriel Keith
 */
class CharacterMovementCom : Component() {
    var groundSpeed: Float = 0f
    var airSpeed: Float = 0f
    var airDamping: Float = 0f

    var jumpCooldown: Float = 0f
    var jumpForce: Float = 0f
}
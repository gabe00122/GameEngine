package gabek.sm2.components.character

import com.artemis.Component
import com.artemis.PooledComponent

/**
 * @author Gabriel Keith
 */
class MovementDefinitionCom : PooledComponent() {
    var groundSpeed: Float = 0f
    var airSpeed: Float = 0f
    var airDamping: Float = 0f

    var jumpCooldown: Float = 0f
    var jumpForce: Float = 0f

    var pad = 0f
    var width = 0f
    var height = 0f

    override fun reset() {
        groundSpeed = 0f
        airSpeed = 0f
        airDamping = 0f

        jumpCooldown = 0f
        jumpForce = 0f

        pad = 0f
        width = 0f
        height = 0f
    }
}
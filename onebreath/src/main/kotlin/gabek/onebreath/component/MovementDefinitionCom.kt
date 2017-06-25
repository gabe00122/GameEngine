package gabek.onebreath.component

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */
class MovementDefinitionCom : RComponent<MovementDefinitionCom>() {
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

    override fun set(other: MovementDefinitionCom) {
        groundSpeed = other.groundSpeed
        airSpeed = other.airSpeed
        airDamping = other.airDamping

        jumpCooldown = other.jumpCooldown
        jumpForce = other.jumpForce

        pad = other.pad
        width = other.width
        height = other.height
    }

    override fun toString() = "MovementDefinitionCom: groundSpeed = $groundSpeed, airSpeed = $airSpeed, airDamping = $airDamping, " +
            "jumpCooldown = $jumpCooldown, jumpForce = $jumpForce, pad = $pad, width = $width, height = $height"

    enum class Strategy {
        ADVANCED, SIMPLE
    }
}
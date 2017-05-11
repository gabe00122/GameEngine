package gabek.engine.core.components.pellet

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class PelletMovementCom: RComponent<PelletMovementCom>() {
    var gravity: Float = 0f
    var speedX: Float = 0f
    var speedY: Float = 0f

    override fun reset() {
        gravity = 0f
        speedX = 0f
        speedY = 0f
    }

    override fun set(other: PelletMovementCom) {
        gravity = other.gravity
        speedX = other.speedX
        speedY = other.speedY
    }

    override fun toString() = "PelletMovementCom: gravity = $gravity, speedX = $speedX, speedY = $speedY"
}
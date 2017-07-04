package gabek.engine.core.components.common

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 * @date 6/30/2017
 */
class VelocityCom: RComponent<VelocityCom>() {
    var xSpeed: Float = 0f
    var ySpeed: Float = 0f


    override fun set(other: VelocityCom) {
        xSpeed = other.xSpeed
        ySpeed = other.ySpeed
    }

    override fun reset() {
        xSpeed = 0f
        ySpeed = 0f
    }
}
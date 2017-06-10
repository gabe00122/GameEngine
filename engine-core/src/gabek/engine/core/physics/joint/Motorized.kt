package gabek.engine.core.physics.joint

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */

interface Motorized {
    var isMotorEnabled: Boolean
    var motorSpeed: Float

    fun reverseDirection() {
        motorSpeed = -motorSpeed
    }
}
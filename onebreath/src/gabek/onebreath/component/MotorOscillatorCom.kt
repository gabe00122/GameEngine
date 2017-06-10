package gabek.onebreath.component

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */

class MotorOscillatorCom: RComponent<MotorOscillatorCom>() {
    var target: Float = 0f

    override fun set(other: MotorOscillatorCom) {
        target = other.target
    }

    override fun reset() {
        target = 0f
    }
}
package gabek.onebreath.component

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */

class HealthCom : RComponent<HealthCom>() {
    var maximum: Float = 0f
    var value: Float = 0f

    var cooldown: Float = 0f

    override fun set(other: HealthCom) {
        maximum = other.maximum
        value = other.value

        cooldown = other.cooldown
    }

    override fun reset() {
        maximum = 0f
        value = 0f
        cooldown = 0f
    }

    override fun toString() = "HealthCom: health = $value, maximum = $maximum, cooldown = $cooldown"
}
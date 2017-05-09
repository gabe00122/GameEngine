package gabek.engine.components.graphics

import gabek.engine.components.RComponent

/**
 * @author Gabriel Keith
 */
class HealthDisplayCom : RComponent<HealthDisplayCom>() {
    var viable = true

    var offsetX = 0f
    var offsetY = 0f

    override fun set(other: HealthDisplayCom) {
        viable = other.viable

        offsetX = other.offsetX
        offsetY = other.offsetY
    }

    override fun reset() {
        viable = true

        offsetX = 0f
        offsetY = 0f
    }

    override fun toString() = "HealthDisplayCom: viable = $viable, offsetX = $offsetX, offsetY = $offsetY"
}
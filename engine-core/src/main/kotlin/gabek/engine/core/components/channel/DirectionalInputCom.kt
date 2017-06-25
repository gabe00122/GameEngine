package gabek.engine.core.components.channel

import gabek.engine.core.components.RComponent

/**
 * @another Gabriel Keith
 * @date 5/18/2017.
 */

class DirectionalInputCom: RComponent<DirectionalInputCom>(){
    var panX: Float = 0f
    var panY: Float = 0f

    override fun set(other: DirectionalInputCom) {
        panX = other.panX
        panY = other.panY
    }

    override fun reset() {
        panX = 0f
        panY = 0f
    }
}
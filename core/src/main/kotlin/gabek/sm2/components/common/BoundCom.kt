package gabek.sm2.components.common

import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class BoundCom: RComponent<BoundCom>(){
    var width = 0f
    var height = 0f

    override fun set(other: BoundCom) {
        width = other.width
        height = other.height
    }

    override fun reset() {
        width = 0f
        height = 0f
    }

    override fun toString(): String {
        return "BoundCom: width = $width, height = $height"
    }
}
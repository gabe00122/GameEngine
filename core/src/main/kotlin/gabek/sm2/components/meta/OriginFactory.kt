package gabek.sm2.components.meta

import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */

class OriginFactory: RComponent<OriginFactory>(){
    var factoryName = ""

    override fun set(other: OriginFactory) {
        factoryName = other.factoryName
    }

    override fun reset() {
        factoryName = ""
    }
}
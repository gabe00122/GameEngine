package gabek.engine.core.components.meta

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */

class PrefabCom: RComponent<PrefabCom>() {
    var name = ""

    override fun set(other: PrefabCom) {
        name = other.name
    }

    override fun reset() {
        name = ""
    }

    override fun toString() = "PrefabCom: name = $name"
}
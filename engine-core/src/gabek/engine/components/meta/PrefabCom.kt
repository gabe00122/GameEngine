package gabek.engine.components.meta

import gabek.engine.components.RComponent

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */

class PrefabCom : RComponent<PrefabCom>() {
    var name = ""

    override fun set(other: PrefabCom) {
        name = other.name
    }

    override fun reset() {
        name = ""
    }

    override fun toString() = "PrefabCom: name = $name"
}
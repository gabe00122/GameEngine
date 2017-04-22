package gabek.sm2.components.common

import com.artemis.annotations.EntityId
import gabek.sm2.components.RComponent

/**
 * @author Gabriel Keith
 */
class ParentOfCom : RComponent<ParentOfCom>() {
    @JvmField @EntityId var parent: Int = -1

    var diesWithParent: Boolean = false

    override fun set(other: ParentOfCom) {
        parent = other.parent
        diesWithParent = other.diesWithParent
    }

    override fun reset() {
        parent = -1
        diesWithParent = false
    }

    override fun toString() = "ParentOfCom: parent = $parent, diesWithParent = $diesWithParent"
}
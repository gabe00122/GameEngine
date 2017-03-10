package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.EntityId

/**
 * @author Gabriel Keith
 */
class ParentOfCom : Component() {
    @JvmField @EntityId var parent: Int = -1

    var diesWithParent: Boolean = false
}
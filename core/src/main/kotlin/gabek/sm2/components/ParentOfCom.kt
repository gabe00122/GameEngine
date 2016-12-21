package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.EntityId
import com.artemis.annotations.PooledWeaver

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class ParentOfCom : Component(){
    @JvmField
    @EntityId var parent: Int = -1
}
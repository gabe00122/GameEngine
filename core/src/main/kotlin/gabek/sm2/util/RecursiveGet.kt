package gabek.sm2.util

import com.artemis.Component
import com.artemis.ComponentMapper
import gabek.sm2.components.ParentOfCom

/**
 * @author Gabriel Keith
 */

fun <T : Component> recursiveGet(parentOf: ComponentMapper<ParentOfCom>, mapper: ComponentMapper<T>, entityId: Int): T?{
    if(mapper.has(entityId)){
        return mapper[entityId]
    } else if(parentOf.has(entityId)){
        return recursiveGet(parentOf, mapper, parentOf[entityId].parent)
    } else {
        return null
    }
}
package gabek.sm2.util

import com.artemis.Component
import com.artemis.ComponentMapper
import gabek.sm2.components.ParentOfCom

/**
 * @author Gabriel Keith
 */
class RecursiveMapper<A: Component>(val parentMapper: ComponentMapper<ParentOfCom>, val mapper: ComponentMapper<A>){

    //fun get(entity: Int): A = safeGet(entity) ?: throw IllegalStateException("nether entity or the parents of ann entity have ")

    fun safeGet(entity: Int): A? {
        if(mapper.has(entity)){
            return mapper[entity]
        } else if(parentMapper.has(entity)){
            return safeGet(parentMapper[entity].parent)
        } else {
            return null
        }
    }

}
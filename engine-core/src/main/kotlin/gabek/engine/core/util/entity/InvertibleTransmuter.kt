package gabek.engine.core.util.entity

import com.artemis.Entity
import com.artemis.EntityTransmuter

/**
 * @another Gabriel Keith
 * @date 5/25/2017.
 */

class InvertibleTransmuter internal constructor(
        private val transmuter: EntityTransmuter,
        private val invertedTransmuter: EntityTransmuter
) {
    fun transmute(entityId: Int){
        transmuter.transmute(entityId)
    }

    fun transmute(entity: Entity){
        transmuter.transmute(entity)
    }

    fun inverse(entityId: Int){
        invertedTransmuter.transmute(entityId)
    }

    fun inverse(entity: Entity){
        invertedTransmuter.transmute(entity)
    }
}
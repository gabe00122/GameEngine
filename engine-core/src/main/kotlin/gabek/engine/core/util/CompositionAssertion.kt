package gabek.engine.core.util

import com.artemis.Component
import com.artemis.ComponentMapper

/**
 * @another Gabriel Keith
 * @date 5/25/2017.
 */

class CompositionAssertion{
    val hasList = ArrayList<ComponentMapper<Component>>()

    fun check(entityId: Int){
        for(h in hasList){
            if(!h.has(entityId)){
                println("Composition check failed for entity: $entityId")
                break
            }
        }
    }
}
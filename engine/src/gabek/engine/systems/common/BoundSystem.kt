package gabek.sm2.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.common.BoundCom

/**
 * @author Gabriel Keith
 * @date 4/28/2017
 */

class BoundSystem : BaseEntitySystem(Aspect.all(BoundCom::class.java)) {
    private lateinit var boundMapper: ComponentMapper<BoundCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val bound = boundMapper[entity]

            bound.pWidth = bound.width
            bound.pHeight = bound.height
        }
    }
}
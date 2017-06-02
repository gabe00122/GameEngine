package gabek.engine.core.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.common.SizeCom

/**
 * @author Gabriel Keith
 * @date 4/28/2017
 */

class BoundSystem : BaseEntitySystem(Aspect.all(SizeCom::class.java)) {
    private lateinit var sizeMapper: ComponentMapper<SizeCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val bound = sizeMapper[entity]

            bound.pWidth = bound.width
            bound.pHeight = bound.height
        }
    }
}
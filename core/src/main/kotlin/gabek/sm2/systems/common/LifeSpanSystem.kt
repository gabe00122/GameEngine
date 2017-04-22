package gabek.sm2.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.LifeSpanCom

/**
 * @author Gabriel Keith
 */
class LifeSpanSystem : BaseEntitySystem(Aspect.all(LifeSpanCom::class.java)) {
    private lateinit var lifeSpanMapper: ComponentMapper<LifeSpanCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val lifespan = lifeSpanMapper[entity]
            lifespan.lifeSpan -= world.delta
            if (lifespan.lifeSpan <= 0f) {
                world.delete(entity)
            }
        }
    }

}
package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.PelletLifeSpanCom

/**
 * @author Gabriel Keith
 */
class PelletLifeSpanSystem: BaseEntitySystem(Aspect.all(PelletLifeSpanCom::class.java)){
    private lateinit var lifeSpanMapper: ComponentMapper<PelletLifeSpanCom>

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val lifespan = lifeSpanMapper[entity]
            lifespan.lifeSpan -= world.delta
            if(lifespan.lifeSpan <= 0f){
                world.delete(entity)
            }
        }
    }

}
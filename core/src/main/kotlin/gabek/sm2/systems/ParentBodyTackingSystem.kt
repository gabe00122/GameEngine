package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.BodyCom
import gabek.sm2.components.ParentOfCom
import gabek.sm2.components.ParentOffsetCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class ParentBodyTackingSystem : BaseEntitySystem(Aspect.all(
        ParentOfCom::class.java, ParentOffsetCom::class.java, BodyCom::class.java)){

    private lateinit var parentMapper: ComponentMapper<ParentOfCom>
    private lateinit var offsetMapper: ComponentMapper<ParentOffsetCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val parent = parentMapper[entity].parent
            val offset = offsetMapper[entity]
            val body = bodyMapper[entity].rBody

            if(parent != -1 && bodyMapper.has(parent)) {
                val pBody = bodyMapper[parent].rBody
                body.setPosition(pBody.x + offset.x, pBody.y + offset.y)
            }
        }
    }
}
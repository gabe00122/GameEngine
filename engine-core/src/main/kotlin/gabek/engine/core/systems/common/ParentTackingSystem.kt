package gabek.engine.core.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.common.ParentOfCom
import gabek.engine.core.components.common.ParentOffsetCom
import gabek.engine.core.components.common.TranslationCom

/**
 * @author Gabriel Keith
 */
class ParentTackingSystem : BaseEntitySystem(
        Aspect.all(
                TranslationCom::class.java,
                ParentOfCom::class.java,
                ParentOffsetCom::class.java
        )) {

    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var parentMapper: ComponentMapper<ParentOfCom>
    private lateinit var offsetMapper: ComponentMapper<ParentOffsetCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val trans = transMapper[entity]
            val parent = parentMapper[entity].parent
            val offset = offsetMapper[entity]

            if (transMapper.has(parent)) {
                val parentTrans = transMapper[parent]
                trans.x = parentTrans.x + offset.x
                trans.y = parentTrans.y + offset.y
                trans.rotation = parentTrans.rotation
            }
        }
    }
}
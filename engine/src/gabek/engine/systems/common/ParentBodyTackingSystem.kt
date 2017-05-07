package gabek.sm2.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.common.ParentOfCom
import gabek.sm2.components.common.ParentOffsetCom
import gabek.sm2.components.common.TranslationCom

/**
 * @author Gabriel Keith
 */
class ParentBodyTackingSystem : BaseEntitySystem(Aspect.all(
        TranslationCom::class.java, ParentOfCom::class.java, ParentOffsetCom::class.java)) {

    private lateinit var transSystem: TranslationSystem
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
                transSystem.teleport(entity,
                        parentTrans.x + offset.x,
                        parentTrans.y + offset.y,
                        trans.rotation, smooth = true)
            }
        }
    }
}
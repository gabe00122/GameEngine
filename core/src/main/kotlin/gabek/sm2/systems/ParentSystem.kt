package gabek.sm2.systems

import com.artemis.BaseSystem
import com.artemis.Component
import com.artemis.ComponentMapper
import com.artemis.link.EntityLinkManager
import com.artemis.link.LinkAdapter
import gabek.sm2.components.ParentOfCom

/**
 * @author Gabriel Keith
 */
class ParentSystem : BaseSystem() {
    private lateinit var entityLinkManager: EntityLinkManager
    private lateinit var parentOfMapper: ComponentMapper<ParentOfCom>

    override fun initialize() {
        super.initialize()

        entityLinkManager.register(ParentOfCom::class.java, object : LinkAdapter() {
            override fun onTargetDead(sourceId: Int, deadTargetId: Int) {
                if (parentOfMapper[sourceId].diesWithParent) {
                    world.delete(sourceId)
                }
            }
        })
    }

    override fun processSystem() {}

    fun getParent(entity: Int): Int {
        return parentOfMapper[entity].parent
    }

    fun <T : Component> recursiveGet(mapper: ComponentMapper<T>, entity: Int): T? {
        if (mapper.has(entity)) {
            return mapper[entity]
        } else if (parentOfMapper.has(entity)) {
            return recursiveGet(mapper, parentOfMapper[entity].parent)
        } else {
            return null
        }
    }
}
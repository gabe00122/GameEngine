package gabek.engine.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.components.common.TranslationCom

/**
 * @author Gabriel Keith
 */
class TranslationSystem : BaseEntitySystem(Aspect.all(TranslationCom::class.java)) {
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    private var teleportListeners = mutableListOf<TeleportListener>()

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            with(translationMapper.get(entities[i])) {
                pX = x
                pY = y
                pRotation = rotation
            }
        }
    }

    fun addTeleportListener(teleportListener: TeleportListener) {
        teleportListeners.add(teleportListener)
    }

    /**
     * Moves entities translation somewhere as well as any other components dependent on location
     */
    fun teleport(entity: Int, x: Float, y: Float, rotation: Float, smooth: Boolean = false) {
        assert(translationMapper.has(entity))
        for (listener in teleportListeners) {
            listener.onTeleport(entity, x, y, rotation, smooth)
        }


        val trans = translationMapper[entity]
        trans.x = x
        trans.y = y
        trans.rotation = rotation

        if (!smooth) {
            trans.pX = x
            trans.pY = y
            trans.pRotation = rotation
        }
    }

    fun teleportRelative(entity: Int, x: Float, y: Float, rotation: Float, smooth: Boolean = false) {
        val trans = translationMapper[entity]
        teleport(entity, x + trans.x, y + trans.y, rotation + trans.rotation, smooth)
    }

    fun teleportChild(parent: Int, child: Int, x: Float, y: Float, rotation: Float, smooth: Boolean = false) {
        if (child != -1) {
            val trans = translationMapper[parent]
            teleportRelative(child, x - trans.x, y - trans.y, rotation - trans.rotation, smooth)
        }
    }

    fun getTranslation(entity: Int): TranslationCom {
        return translationMapper[entity]
    }

    interface TeleportListener {
        fun onTeleport(id: Int, x: Float, y: Float, rotation: Float, smooth: Boolean)
    }
}
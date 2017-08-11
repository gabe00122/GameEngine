package gabek.engine.core.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.common.TranslationCom

/**
 * @author Gabriel Keith
 */
class TranslationSystem : BaseEntitySystem(Aspect.all(TranslationCom::class.java)) {
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

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

    fun setPosition(entity: Int, x: Float, y: Float){
        val trans = translationMapper[entity]
        trans.x = x
        trans.y = y
    }

    fun getTranslation(entity: Int): TranslationCom {
        return translationMapper[entity]
    }
}
package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.TranslationCom

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
}
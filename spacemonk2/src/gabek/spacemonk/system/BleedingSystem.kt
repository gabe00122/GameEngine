package gabek.spacemonk.system

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.character.CharacterControllerCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.prefab.Prefab
import gabek.engine.core.systems.common.PrefabManager
import java.util.*

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class BleedingSystem: BaseEntitySystem(Aspect.all(CharacterControllerCom::class.java)) {
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var prefabManager: PrefabManager
    private lateinit var bloodFactory: Prefab

    private val random = Random()

    override fun initialize() {
        super.initialize()

        bloodFactory = prefabManager.getPrefab("blood")
    }

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val trans = transMapper[entity]

            if (random.nextFloat() < 0.1f) {
                bloodFactory.create(trans.x, trans.y)
            }
        }
    }
}
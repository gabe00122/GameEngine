package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.prefab.Prefab
import gabek.sm2.systems.common.PrefabManager
import java.util.*

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class BleedingSystem : BaseEntitySystem(Aspect.all(CharacterControllerCom::class.java)) {
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
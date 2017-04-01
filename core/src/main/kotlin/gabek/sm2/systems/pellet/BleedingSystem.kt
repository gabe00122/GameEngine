package gabek.sm2.systems.pellet

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.factory.EntityFactory
import gabek.sm2.systems.FactoryManager
import gabek.sm2.systems.TranslationSystem
import java.util.*

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class BleedingSystem: BaseEntitySystem(Aspect.all(CharacterControllerCom::class.java)){
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var factoryManager: FactoryManager
    private lateinit var bloodFactory: EntityFactory

    private val random = Random()

    override fun initialize() {
        super.initialize()

        bloodFactory = factoryManager.getFactory("blood")
    }

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]
            val trans = transMapper[entity]

            if(random.nextFloat() < 0.1f) {
                bloodFactory.create(trans.x, trans.y)
            }
        }
    }
}
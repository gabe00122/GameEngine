package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.AbilityIndexCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.components.character.CharacterStateCom
import gabek.sm2.systems.FactoryManager

/**
 * @author Gabriel Keith
 */
class AbilityIndexSystem: BaseEntitySystem(Aspect.all(
        TranslationCom::class.java,
        AbilityIndexCom::class.java,
        CharacterStateCom::class.java,
        CharacterControllerCom::class.java)){

    private lateinit var factoryManager: FactoryManager
    //private lateinit var pelletFactory: PelletFactory
    //private lateinit var punchFactory: BasicPunchFactory

    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var abilityIndexMapper: ComponentMapper<AbilityIndexCom>
    private lateinit var characterStateMapper: ComponentMapper<CharacterStateCom>
    private lateinit var controlMapper: ComponentMapper<CharacterControllerCom>

    override fun initialize() {
        super.initialize()

        //pelletFactory = factoryManager.getFactory(PelletFactory::class.java)
        //punchFactory = factoryManager.getFactory(BasicPunchFactory::class.java)
    }

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val trans = transMapper[entity]
            val abilityIndex = abilityIndexMapper[entity]
            val state = characterStateMapper[entity]
            val control = controlMapper[entity]

            if(abilityIndex.moveTimeout > 0) {
                abilityIndex.moveTimeout -= world.delta
            }

            if(control.primary && abilityIndex.moveTimeout <= 0f){
                abilityIndex.moveTimeout += 0.1f

                if(state.direction == CharacterStateCom.Direction.LEFT){
                    //pelletFactory.create(trans.x - 0.5f, trans.y, 180f, 2.5f, 0.1f)
                    //punchFactory.create(entity, -0.5f, 0f, 0.1f)
                }
                if(state.direction == CharacterStateCom.Direction.RIGHT){
                    //pelletFactory.create(trans.x + 0.5f, trans.y, 0f, 2.5f, 0.1f)
                    //punchFactory.create(entity, 0.5f, 0f, 0.1f)
                }
            }
        }
    }

}

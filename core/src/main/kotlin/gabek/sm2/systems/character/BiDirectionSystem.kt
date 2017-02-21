package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.character.BiDirectionCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.util.FSMTransitionTable
import gabek.sm2.components.character.BiDirectionCom.Direction.*

/**
 * @author Gabriel Keith
 */
class BiDirectionSystem: BaseEntitySystem(Aspect.all(BiDirectionCom::class.java)){
    private lateinit var biDirectionMapper: ComponentMapper<BiDirectionCom>
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>

    private val transitionMap = FSMTransitionTable(BiDirectionCom.Direction::class)
    { entity, state ->
        val biDirection = biDirectionMapper[entity]
        biDirection.direction = state
        biDirection.timeInState = 0
    }

    override fun initialize() {
        super.initialize()

        transitionMap.addListener(LEFT, RIGHT){ entity, from, to ->
            if(spriteMapper.has(entity)){
                spriteMapper[entity].flipX = true
            }
        }

        transitionMap.addListener(RIGHT, LEFT){ entity, from, to ->
            if(spriteMapper.has(entity)){
                spriteMapper[entity].flipX = false
            }
        }
    }

    fun setDirection(entity: Int, direction: BiDirectionCom.Direction){
        transitionMap.transition(entity, biDirectionMapper[entity].direction, direction)
    }

    override fun processSystem() {}
}
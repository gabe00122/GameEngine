package gabek.engine.core.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.character.BiDirectionCom.Direction.*
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.util.FSMTransitionTable

/**
 * @author Gabriel Keith
 */
class BiDirectionSystem: BaseEntitySystem(Aspect.all(BiDirectionCom::class.java)) {
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

        transitionMap.addListener(LEFT, RIGHT) { entity, from, to ->
            if (spriteMapper.has(entity)) {
                spriteMapper[entity].flipX = !biDirectionMapper[entity].invert
            }
        }

        transitionMap.addListener(RIGHT, LEFT) { entity, from, to ->
            if (spriteMapper.has(entity)) {
                spriteMapper[entity].flipX = biDirectionMapper[entity].invert
            }
        }
    }

    override fun inserted(entityId: Int) {
        super.inserted(entityId)

        if(biDirectionMapper[entityId].invert && spriteMapper.has(entityId)){
            spriteMapper[entityId].flipX = true
        }
    }

    fun setDirection(entity: Int, direction: BiDirectionCom.Direction) {
        transitionMap.transition(entity, biDirectionMapper[entity].direction, direction)
    }

    override fun processSystem() {}
}
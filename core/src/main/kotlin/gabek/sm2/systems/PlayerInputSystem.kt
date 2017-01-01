package gabek.sm2.systems

import com.artemis.*
import com.artemis.utils.IntBag
import com.badlogic.gdx.utils.IntMap
import gabek.sm2.components.CharacterControllerCom
import gabek.sm2.components.PlayerInputCom
import gabek.sm2.input.Actions
import gabek.sm2.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class PlayerInputSystem : BaseEntitySystem(Aspect.all(PlayerInputCom::class.java, CharacterControllerCom::class.java)){
    private lateinit var playerInputMapper: ComponentMapper<PlayerInputCom>
    private lateinit var characterControlMapper: ComponentMapper<CharacterControllerCom>

    override fun processSystem() {
        val entities = entityIds
        for(i in 0 until entities.size()){
            val id = entities[i]
            val char = characterControlMapper[id]
            val playerInput = playerInputMapper[id].playerInput

            if(playerInput != null){
                playerInput.update(world.delta)

                char.moveUp = playerInput.pollAction(Actions.UP)
                char.moveDown = playerInput.pollAction(Actions.DOWN)
                char.moveLeft = playerInput.pollAction(Actions.LEFT)
                char.moveRight = playerInput.pollAction(Actions.RIGHT)
            }
        }
    }
}
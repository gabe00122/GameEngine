package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.PlayerInputCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.input.Actions
import gabek.sm2.input.PlayerInput

/**
 * @author Gabriel Keith
 */
class PlayerInputSystem : BaseEntitySystem(Aspect.all(PlayerInputCom::class.java, CharacterControllerCom::class.java)) {
    private lateinit var playerInputMapper: ComponentMapper<PlayerInputCom>
    private lateinit var characterControlMapper: ComponentMapper<CharacterControllerCom>

    override fun processSystem() {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val id = entities[i]
            val char = characterControlMapper[id]
            val playerInput = playerInputMapper[id].playerInput

            if (playerInput != null) {
                char.moveUp = playerInput.pollAction(Actions.UP)
                char.moveDown = playerInput.pollAction(Actions.DOWN)

                if (!(char.moveRight && playerInput.pollAction(Actions.RIGHT))
                        && playerInput.pollAction(Actions.LEFT)) {
                    char.lateralMovement = -1f
                } else if (playerInput.pollAction(Actions.RIGHT)) {
                    char.lateralMovement = 1f
                } else {
                    char.lateralMovement = 0f
                }

                char.primary = playerInput.pollAction(Actions.SELECT)
            }
        }
    }

    fun setInput(entity: Int, playerInput: PlayerInput) {
        playerInputMapper[entity].playerInput = playerInput
    }
}
package gabek.spacemonk.system

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.spacemonk.component.WanderingBrainCom
import gabek.engine.core.components.character.CharacterControllerCom
import gabek.engine.core.systems.common.UpdateManager
import java.util.*

/**
 * @author Gabriel Keith
 */
class WanderingBrainSystem : BaseEntitySystem(Aspect.all(
        WanderingBrainCom::class.java,
        CharacterControllerCom::class.java)) {
    private lateinit var updateManager: UpdateManager
    private lateinit var wanderingBrain: ComponentMapper<WanderingBrainCom>
    private lateinit var controllerMapper: ComponentMapper<CharacterControllerCom>
    private val random = Random()

    override fun processSystem() {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val brain = wanderingBrain[entity]
            val controller = controllerMapper[entity]

            controller.moveUp = false

            if (updateManager.getElapsedTime(brain.timeToTurn) >= 0) {
                brain.timeToTurn = updateManager.currentFrame + 60 * 5
                if (controller.moveLeft) {
                    if (random.nextBoolean()) {
                        controller.moveUp = true
                    }
                    controller.moveLeft = false
                    if (random.nextBoolean()) {
                        controller.moveRight = true
                    }
                } else if (controller.moveRight) {
                    if (random.nextBoolean()) {
                        controller.moveUp = true
                    }
                    if (random.nextBoolean()) {
                        controller.moveLeft = true
                    }
                    controller.moveRight = false
                } else {
                    if (random.nextBoolean()) {
                        controller.moveLeft = true
                    } else {
                        controller.moveRight = true
                    }
                }
            }
        }
    }

}
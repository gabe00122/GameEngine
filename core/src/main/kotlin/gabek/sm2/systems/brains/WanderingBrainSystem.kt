package gabek.sm2.systems.brains

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.brains.WanderingBrainCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.systems.common.TimeManager
import java.util.*

/**
 * @author Gabriel Keith
 */
class WanderingBrainSystem : BaseEntitySystem(Aspect.all(
        WanderingBrainCom::class.java,
        CharacterControllerCom::class.java)) {
    private lateinit var timeManager: TimeManager
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

            if (timeManager.getElapsedTime(brain.timeToTurn) >= 0) {
                brain.timeToTurn = timeManager.currentFrame + 60 * 5
                if (controller.moveLeft) {
                    if(random.nextBoolean()){
                        controller.moveUp = true
                    }
                    controller.moveLeft = false
                    if (random.nextBoolean()) {
                        controller.moveRight = true
                    }
                } else if (controller.moveRight) {
                    if(random.nextBoolean()){
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
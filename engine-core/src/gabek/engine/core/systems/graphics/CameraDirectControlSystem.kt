package gabek.engine.core.systems.graphics

import com.artemis.*
import gabek.engine.core.components.InputCom
import gabek.engine.core.components.channel.DirectionalInputCom
import gabek.engine.core.components.common.BoundCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.CameraCom
import gabek.engine.core.input.PlayerInput

/**
 * @another Gabriel Keith
 * @date 5/23/2017.
 */

class CameraDirectControlSystem: BaseEntitySystem(
        Aspect.all(
                CameraCom::class.java,
                TranslationCom::class.java,
                BoundCom::class.java,
                DirectionalInputCom::class.java
        )) {
    private lateinit var cameraMapper: ComponentMapper<CameraCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var boundMapper: ComponentMapper<BoundCom>
    private lateinit var inputMapper: ComponentMapper<InputCom>
    private lateinit var directionalInputMapper: ComponentMapper<DirectionalInputCom>

    private lateinit var controlTrans: EntityTransmuter

    override fun initialize() {
        super.initialize()

        controlTrans = EntityTransmuterFactory(world)
                .add(InputCom::class.java)
                .add(DirectionalInputCom::class.java)
                .build()

    }

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val camera = cameraMapper[entity]
            val trans = transMapper[entity]
            val bound = boundMapper[entity]
            val dirInput = directionalInputMapper[entity]

            trans.x += dirInput.panX * world.delta
            trans.y += dirInput.panY * world.delta
        }
    }


    fun addInputController(cameraId: Int, playerInput: PlayerInput){
        controlTrans.transmute(cameraId)
        val input = inputMapper[cameraId]
        input.input = playerInput
    }
}
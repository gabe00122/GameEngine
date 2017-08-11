package gabek.engine.core.systems.graphics

import com.artemis.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import gabek.engine.core.components.InputCom
import gabek.engine.core.components.channel.DirectionalInputCom
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.common.VelocityCom
import gabek.engine.core.components.graphics.CameraCom
import gabek.engine.core.input.PlayerInput
import gabek.engine.core.util.entity.InvertibleTransmuter
import gabek.engine.core.util.entity.InvertibleTransmuterFactory

/**
 * @another Gabriel Keith
 * @date 5/23/2017.
 */

class CameraDirectControlSystem: BaseEntitySystem(
        Aspect.all(
                CameraCom::class.java,
                TranslationCom::class.java,
                VelocityCom::class.java,
                SizeCom::class.java,
                DirectionalInputCom::class.java
        )) {
    private lateinit var cameraMapper: ComponentMapper<CameraCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var velocityMapper: ComponentMapper<VelocityCom>
    private lateinit var sizeMapper: ComponentMapper<SizeCom>
    private lateinit var inputMapper: ComponentMapper<InputCom>
    private lateinit var directionalInputMapper: ComponentMapper<DirectionalInputCom>

    private lateinit var controlTrans: InvertibleTransmuter

    var aceleration = 10f
    var maxSpeed = 7.5f

    override fun initialize() {
        super.initialize()

        controlTrans = InvertibleTransmuterFactory(world)
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
            val velocity = velocityMapper[entity]
            val bound = sizeMapper[entity]
            val dirInput = directionalInputMapper[entity]

            if(Math.abs(velocity.xSpeed) < maxSpeed) {
                velocity.xSpeed += dirInput.panX * world.delta * aceleration
            }
            if(Math.abs(velocity.ySpeed) < maxSpeed) {
                velocity.ySpeed += dirInput.panY * world.delta * aceleration
            }

            if(dirInput.panX == 0f) {
                velocity.xSpeed *= 1f - (2f * world.delta)
            }

            if(dirInput.panY == 0f) {
                velocity.ySpeed *= 1f - (2f * world.delta)
            }

            trans.x += velocity.xSpeed * world.delta
            trans.y += velocity.ySpeed * world.delta

            if(Gdx.input.isKeyPressed(Input.Keys.Z)){
                bound.width *= 0.98f
                bound.height *= 0.98f
            }

            if(Gdx.input.isKeyPressed(Input.Keys.X)){
                bound.width /= 0.98f
                bound.height /= 0.98f
            }
        }
    }

    fun addDirectControl(cameraId: Int, playerInput: PlayerInput){
        controlTrans.transmute(cameraId)
        val input = inputMapper[cameraId]
        input.input = playerInput
    }

    fun removeDirectControl(cameraId: Int){
        controlTrans.inverse(cameraId)
    }
}
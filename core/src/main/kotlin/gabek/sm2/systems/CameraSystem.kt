package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import gabek.sm2.components.CameraCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class CameraSystem : BaseEntitySystem(Aspect.all(CameraCom::class.java, TranslationCom::class.java)){
    private lateinit var cameraMapper: ComponentMapper<CameraCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    override fun processSystem() {}

    fun updateProjection(ortho: OrthographicCamera, progress: Float, screenWidth: Float, screenHeight: Float){
        val entities = entityIds
        if(!entities.isEmpty){
            val entity = entities[0]
            val camera = cameraMapper.get(entity)
            val trans = translationMapper.get(entity)

            ortho.position.set(trans.lerpX(progress), trans.lerpY(progress), 0f)
            ortho.viewportWidth = camera.viewportWidth
            ortho.viewportHeight = camera.viewportHeight

            //expand viewport.
            val xScale = screenWidth / ortho.viewportWidth
            val yScale = screenHeight / ortho.viewportHeight
            if (xScale < yScale) {
                ortho.viewportHeight *= (yScale / xScale)
            } else {
                ortho.viewportWidth *= (xScale / yScale)
            }

            ortho.update()
        }
    }
}

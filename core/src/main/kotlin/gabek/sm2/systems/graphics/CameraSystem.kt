package gabek.sm2.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.OrthographicCamera
import gabek.sm2.components.graphics.CameraCom

/**
 * @author Gabriel Keith
 */
class CameraSystem : BaseEntitySystem(Aspect.all(CameraCom::class.java)) {
    private lateinit var cameraMapper: ComponentMapper<CameraCom>

    private val ortho = OrthographicCamera()

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val camera = cameraMapper[entity]

            camera.bottomLeft.flip()
            camera.topRight.flip()
        }
    }

    fun updateProjection(cameraHandle: Int, progress: Float, screenWidth: Float, screenHeight: Float): OrthographicCamera {
        val camera = cameraMapper.get(cameraHandle)

        val x1 = camera.bottomLeft.lerpX(progress)
        val y1 = camera.bottomLeft.lerpY(progress)
        val x2 = camera.topRight.lerpX(progress)
        val y2 = camera.topRight.lerpY(progress)

        ortho.position.set((x1 + x2) / 2f, (y1 + y2) / 2f, 0f)
        ortho.viewportWidth = x2 - x1
        ortho.viewportHeight = y2 - y1

        //expand viewport.
        val adjustment = camera.aspectRatio / (ortho.viewportWidth / ortho.viewportHeight)


        if (1f < adjustment) {
            ortho.viewportWidth *= adjustment
        } else {
            ortho.viewportHeight /= adjustment
        }

        ortho.update(false)
        return ortho
    }

    fun setAspectRatio(cameraHandle: Int, aspectRatio: Float){
        cameraMapper[cameraHandle].aspectRatio = aspectRatio
    }
}

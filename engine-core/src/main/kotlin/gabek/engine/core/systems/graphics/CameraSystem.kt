package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.OrthographicCamera
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.CameraCom

/**
 * @author Gabriel Keith
 */
class CameraSystem: BaseEntitySystem(
        Aspect.all(
                CameraCom::class.java,
                TranslationCom::class.java,
                SizeCom::class.java
        )) {
    private lateinit var cameraMapper: ComponentMapper<CameraCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var sizeMapper: ComponentMapper<SizeCom>

    override fun processSystem() {}

    fun updateProjection(ortho: OrthographicCamera, cameraHandle: Int, progress: Float) {
        val camera = cameraMapper.get(cameraHandle)
        val trans = transMapper[cameraHandle]
        val bound = sizeMapper[cameraHandle]

        ortho.position.set(trans.lerpX(progress), trans.lerpY(progress), 0f)
        ortho.viewportWidth = bound.lerpWidth(progress)
        ortho.viewportHeight = bound.lerpHeight(progress)

        //expand viewport.
        val adjustment = camera.aspectRatio / (ortho.viewportWidth / ortho.viewportHeight)


        if (1f < adjustment) {
            ortho.viewportWidth *= adjustment
        } else {
            ortho.viewportHeight /= adjustment
        }

        ortho.update(false)
    }

    fun setAspectRatio(cameraHandle: Int, aspectRatio: Float) {
        cameraMapper[cameraHandle].aspectRatio = aspectRatio
    }

    fun setView(entityId: Int, x: Float, y: Float, width: Float, height: Float){
        assert(transMapper.has(entityId))
        assert(sizeMapper.has(entityId))
        assert(cameraMapper.has(entityId))

        val trans = transMapper[entityId]
        val bounds = sizeMapper[entityId]

        trans.x = x
        trans.y = y

        bounds.width = width
        bounds.height = height
    }
}

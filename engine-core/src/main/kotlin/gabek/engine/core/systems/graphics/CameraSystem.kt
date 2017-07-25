package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.CameraCom
import gabek.engine.core.graphics.Display

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

    fun prepareOrtho(ortho: OrthographicCamera, display: Display, progress: Float, update: Boolean = true) {
        val cameraHandle = display.cameraHandle

        val camera = cameraMapper[cameraHandle]
        val trans = transMapper[cameraHandle]
        val bound = sizeMapper[cameraHandle]

        ortho.position.set(trans.lerpX(progress), trans.lerpY(progress), 0f)
        ortho.viewportWidth = bound.width
        ortho.viewportHeight = bound.height

        //expand viewport.
        val adjustment = (display.pixWidth.toFloat() / display.pixHeight.toFloat()) /
                (ortho.viewportWidth / ortho.viewportHeight)


        if (1f < adjustment) {
            ortho.viewportWidth *= adjustment
        } else {
            ortho.viewportHeight /= adjustment
        }

        if(camera.pixelAlignment > 0){
            val bufferMeterW = display.pixWidth * camera.pixelAlignment
            val bufferMeterH = display.pixHeight * camera.pixelAlignment
            ortho.viewportWidth = bufferMeterW / MathUtils.floor(bufferMeterW / ortho.viewportWidth)
            ortho.viewportHeight = bufferMeterH / MathUtils.floor(bufferMeterH / ortho.viewportHeight)
        }

        if(update){
            ortho.update()
        }
    }

    fun setView(entityId: Int, x: Float, y: Float, width: Float, height: Float){
        val trans = transMapper[entityId]
        val bounds = sizeMapper[entityId]

        trans.x = x
        trans.y = y

        bounds.width = width
        bounds.height = height
    }
}

package gabek.engine.core.systems.graphics

import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.graphics.Display
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.graphics.RenderContext
import gabek.engine.core.systems.PassiveSystem

/**
 * @author Gabriel Keith
 */
class RenderManager(
        kodein: Kodein,
        private var setupCode: (RenderManager.(world: World) -> Unit)? = null
): PassiveSystem() {
    private lateinit var cameraSystem: CameraSystem

    var renderSystems: List<RenderSystem> = listOf()
    var directSystems: List<DirectRenderSystem> = listOf()

    private val context = RenderContext()
    private val ortho = OrthographicCamera()

    var clearColor = Color(0f, 0f, 0f, 1f)

    val pixToMeters = kodein.instance<PixelRatio>().pixelToMeters

    override fun initialize() {
        super.initialize()

        setupCode?.invoke(this, world)
    }

    fun render(display: Display, batch: Batch, progress: Float) {
        cameraSystem.prepareOrtho(ortho, display, progress)

        val bufferMeterW = display.pixWidth * pixToMeters
        val bufferMeterH = display.pixHeight * pixToMeters

        ortho.viewportWidth = bufferMeterW / MathUtils.floor(bufferMeterW / ortho.viewportWidth)
        ortho.viewportHeight = bufferMeterH / MathUtils.floor(bufferMeterH / ortho.viewportHeight)

        ortho.update(false)

        updateCulling(ortho, context.culling)
        context.progress = progress

        for (directSystem in directSystems) {
            directSystem.prepare(display, ortho) //just for lighting
        }

        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        display.render {
            Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

            batch.begin()

            for (batchSystem in renderSystems) {
                batchSystem.render(batch, context)
            }

            batch.end()

            for (directSystem in directSystems) {
                directSystem.render(ortho, context)
            }
        }
    }

    private fun updateCulling(ortho: OrthographicCamera, culling: Rectangle) {
        culling.set(ortho.position.x - ortho.viewportWidth / 2f,
                ortho.position.y - ortho.viewportHeight / 2f,
                ortho.viewportWidth, ortho.viewportHeight)
    }

    interface RenderSystem {
        fun render(batch: Batch, context: RenderContext)
    }

    interface DirectRenderSystem {
        fun prepare(display: Display, ortho: OrthographicCamera)
        fun render(ortho: OrthographicCamera, context: RenderContext)
    }
}
package gabek.engine.core.world

import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import gabek.engine.core.graphics.Display
import gabek.engine.core.systems.PassiveSystem
import gabek.engine.core.systems.graphics.CameraSystem

/**
 * @author Gabriel Keith
 */
class RenderManager(
        private var setupCode: (RenderManager.(world: World) -> Unit)? = null
): PassiveSystem() {
    private lateinit var cameraSystem: CameraSystem

    var batchSystems: List<BatchSystem> = listOf()
    var directSystems: List<DirectRenderSystem> = listOf()

    //private val shader = kodein.instance<Assets>().resourceManager.get("shaders/hex.vert", ShaderProgram::class.java)
    private val culling = Rectangle()
    private val ortho = OrthographicCamera()

    var clearColor = Color(1f, 1f, 1f, 1f)

    override fun initialize() {
        super.initialize()

        setupCode?.invoke(this, world)
    }

    fun render(buffer: Display, batch: SpriteBatch, progress: Float) {
        cameraSystem.updateProjection(ortho, buffer.cameraHandle, progress)
        updateCulling(ortho)

        for (directSystem in directSystems) {
            directSystem.prepare(buffer, ortho) //just for lighting
        }

        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        buffer.beginPrimaryBuffer()
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        //batch.shader = shader
        batch.begin()

        for (batchSystem in batchSystems) {
            batchSystem.render(batch, culling, progress)
        }

        batch.end()
        //batch.shader = null

        for (directSystem in directSystems) {
            directSystem.render(ortho, culling, progress)
        }

        buffer.endPrimaryBuffer()
    }

    private fun updateCulling(ortho: OrthographicCamera) {
        culling.set(ortho.position.x - ortho.viewportWidth / 2f,
                ortho.position.y - ortho.viewportHeight / 2f,
                ortho.viewportWidth, ortho.viewportHeight)
    }

    interface BatchSystem {
        fun render(batch: SpriteBatch, culling: Rectangle, progress: Float)
    }

    interface DirectRenderSystem {
        fun prepare(display: Display, ortho: OrthographicCamera)
        fun render(ortho: OrthographicCamera, culling: Rectangle, progress: Float)
    }
}
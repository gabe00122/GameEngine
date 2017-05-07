package gabek.sm2.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.graphics.Display
import gabek.sm2.systems.graphics.CameraSystem

/**
 * @author Gabriel Keith
 */
class RenderManager(val kodein: Kodein,
                    val cameraSystem: CameraSystem,
                    val batchSystems: List<BatchSystem>,
                    val orthoSystems: List<OrthoRenderSystem> = listOf()) {

    //private val shader = kodein.instance<Assets>().resourceManager.get("shaders/hex.vert", ShaderProgram::class.java)
    private val culling = Rectangle()
    private val ortho = OrthographicCamera()

    fun render(buffer: Display, batch: SpriteBatch, progress: Float) {
        cameraSystem.updateProjection(ortho, buffer.cameraHandle, progress)
        updateCulling(ortho)

        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        buffer.beginPrimaryBuffer()
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        //batch.shader = shader
        batch.begin()

        for (batchSystem in batchSystems) {
            batchSystem.render(batch, culling, progress)
        }

        batch.end()
        //batch.shader = null

        for (orthoSystem in orthoSystems) {
            orthoSystem.render(ortho, culling, progress)
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

    interface OrthoRenderSystem {
        fun render(ortho: OrthographicCamera, culling: Rectangle, progress: Float)
    }
}
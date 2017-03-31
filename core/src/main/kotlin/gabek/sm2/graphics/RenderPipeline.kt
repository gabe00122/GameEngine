package gabek.sm2.graphics

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.settings.Settings
import gabek.sm2.systems.graphics.CameraSystem

/**
 * @author Gabriel Keith
 */
class RenderPipeline(kodein: Kodein) : Widget(), Disposable {
    private var primaryBuffer: FrameBuffer? = null
    var cameraHandle: Int = -1
        private set
    var cameraSystem: CameraSystem? = null
        private set
    private var pixWidth: Int = 0
    private var pixHeight: Int = 0

    private val uiScale = kodein.instance<Settings>().getFloat("ui_scale")

    override fun layout() {
        super.layout()
        rebuildBuffers()
    }

    private fun rebuildBuffers() {
        val oldBuffer = primaryBuffer

        if (oldBuffer == null || Math.abs(oldBuffer.width - width) > 10.0 || Math.abs(oldBuffer.height - height) > 10.0) {
            primaryBuffer?.dispose()

            val w = MathUtils.ceil(width * uiScale.value)
            val h = MathUtils.ceil(height * uiScale.value)

            if (w >= 1 && h >= 1) {
                primaryBuffer = FrameBuffer(Pixmap.Format.RGBA8888, w, h, false)
                primaryBuffer!!.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }
            pixWidth = w
            pixHeight = h
            sendRatio()
        }
    }

    private fun sendRatio(){
        val cameraSystem = cameraSystem
        if (cameraSystem != null && cameraHandle != -1) {
            cameraSystem.setAspectRatio(cameraHandle, pixWidth / pixHeight.toFloat())
        }
    }

    fun beginPrimaryBuffer() {
        primaryBuffer?.begin()
    }

    fun endPrimaryBuffer() {
        primaryBuffer?.end()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        primaryBuffer?.let {
            batch.draw(it.colorBufferTexture, x, y, width, height, 0, 0, it.width, it.height, false, true)
        }
    }

    override fun dispose() {
        primaryBuffer?.dispose()
        primaryBuffer = null
    }

    fun setHandle(cameraHandle: Int, cameraSystem: CameraSystem) {
        this.cameraHandle = cameraHandle
        this.cameraSystem = cameraSystem
        sendRatio()
    }
}
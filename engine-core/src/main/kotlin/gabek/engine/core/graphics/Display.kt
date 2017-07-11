package gabek.engine.core.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.settings.Settings
import gabek.engine.core.systems.graphics.CameraSystem

/**
 * @author Gabriel Keith
 */
class Display(kodein: Kodein): Widget(), Disposable {
    private var primaryBuffer: FrameBuffer? = null

    var cameraHandle: Int = -1
    var aspectRatio: Float = 0f
        private set
    var pixWidth: Int = 0
        private set
    var pixHeight: Int = 0
        private set

    private val uiScale = kodein.instance<Settings>().getFloat("ui_scale")

    override fun layout() {
        super.layout()
        rebuildBuffers()
    }

    private fun rebuildBuffers() {
        val oldBuffer = primaryBuffer

        val newWidth = Gdx.graphics.width
        val newHeight = Gdx.graphics.height

        if (oldBuffer == null || Math.abs(newWidth - pixWidth) != 0 || Math.abs(newHeight - pixHeight) != 0) {
            primaryBuffer?.dispose()

            pixWidth = newWidth
            pixHeight = newHeight

            //pixWidth = Gdx.graphics.width
            //pixHeight = Gdx.graphics.height

            if (pixWidth >= 1 && pixHeight >= 1) {
                primaryBuffer = FrameBuffer(Pixmap.Format.RGBA8888, pixWidth, pixHeight, false)
                primaryBuffer!!.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }

            aspectRatio = pixWidth.toFloat() / pixHeight.toFloat()
        }
    }

    fun beginPrimaryBuffer() {
        primaryBuffer!!.begin()
    }

    fun endPrimaryBuffer() {
        primaryBuffer!!.end()
    }

    val hasBuffer get() = primaryBuffer != null

    inline fun render(renderBlock: () -> Unit){
        if(hasBuffer){
            beginPrimaryBuffer()
            renderBlock()
            endPrimaryBuffer()
        }
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
}
package gabek.sm2.graphics

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.Disposable

/**
 * @author Gabriel Keith
 */
class DisplayBuffer : Widget(), Disposable {
    private var primaryBuffer: FrameBuffer? = null

    init {
        //primaryBuffer = FrameBuffer(Pixmap.Format.RGBA8888, )
    }

    private fun rebuildBuffers(){
        val oldBuffer = primaryBuffer

        if(oldBuffer == null || Math.abs(oldBuffer.width - width) > 10.0 || Math.abs(oldBuffer.height - height) > 10.0) {
            primaryBuffer?.dispose()
            if (width >= 1 && height >= 1) {
                primaryBuffer = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)
                primaryBuffer!!.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }
        }
    }

    fun beginPrimaryBuffer(){
        rebuildBuffers()
        primaryBuffer?.begin()
    }

    fun endPrimaryBuffer(){
        primaryBuffer?.end()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        primaryBuffer?.let{
            batch.draw(it.colorBufferTexture, x, y, width, height, 0, 0, it.width, it.height, false, true)
        }
    }

    override fun dispose() {
        primaryBuffer?.dispose()
        primaryBuffer = null
    }
}
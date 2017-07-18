package gabek.engine.core.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer

/**
 * @author Gabriel Keith
 */
class BufferedDisplay: Display() {
    private var primaryBuffer: FrameBuffer? = null

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

            if (pixWidth >= 1 && pixHeight >= 1) {
                primaryBuffer = FrameBuffer(Pixmap.Format.RGBA8888, pixWidth, pixHeight, false)
                primaryBuffer!!.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
            }
        }
    }

    override fun begin() {
        primaryBuffer!!.begin()
    }

    override fun end() {
        primaryBuffer!!.end()
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
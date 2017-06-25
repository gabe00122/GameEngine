package gabek.engine.core.graphics

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.Disposable

/**
 * @author Gabriel Keith
 * @date 4/24/2017
 */

class PingPongBuffer(val format: Pixmap.Format, val width: Int, val height: Int) : Disposable {
    private var previousBuffer: FrameBuffer = FrameBuffer(format, width, height, false)
    private var nextBuffer: FrameBuffer = FrameBuffer(format, width, height, false)

    init {
        previousBuffer.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        nextBuffer.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    }

    fun flip() {
        val temp = previousBuffer
        previousBuffer = nextBuffer
        nextBuffer = temp
    }

    val bufferTexture: Texture = previousBuffer.colorBufferTexture

    fun begin() {
        nextBuffer.begin()
    }

    fun end(flip: Boolean = true) {
        nextBuffer.end()
        if (flip) {
            flip()
        }
    }

    override fun dispose() {
        previousBuffer.dispose()
        nextBuffer.dispose()
    }
}
package gabek.engine.core.graphics

import com.badlogic.gdx.math.Rectangle

/**
 * @author Gabriel Keith
 * @date 6/27/2017
 */

class RenderContext {
    var culling: Rectangle = Rectangle()
    var progress: Float = 0f

    fun set(progress: Float){
        this.progress = progress
    }
}
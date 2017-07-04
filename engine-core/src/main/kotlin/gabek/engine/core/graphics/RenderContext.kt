package gabek.engine.core.graphics

import com.badlogic.gdx.math.Rectangle

/**
 * @author Gabriel Keith
 * @date 6/27/2017
 */

class RenderContext {
    var culling: Rectangle = Rectangle()
    var roundingByX: Float = 0f
    var roundingByY: Float = 0f
    var progress: Float = 0f

    fun set(roundingByX: Float, roundingByY: Float, progress: Float){
        this.roundingByX = roundingByX
        this.roundingByY = roundingByY
        this.progress = progress
    }
}
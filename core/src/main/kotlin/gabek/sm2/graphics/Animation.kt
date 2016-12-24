package gabek.sm2.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * @author Gabriel Keith
 */
class Animation(val delay: Float, val pingpong: Boolean) {
    val frames = mutableListOf<TextureRegion>()
}
package gabek.engine.core.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * @author Gabriel Keith
 */
class Sprite(
        val lookup: String,
        val texture: TextureRegion,
        val offsetX: Float,
        val offsetY: Float
)
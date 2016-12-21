package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class SpriteCom : Component(){
    @JvmField @Transient var texture: TextureRegion? = null

    @JvmField var width = 0f
    @JvmField var height = 0f
}
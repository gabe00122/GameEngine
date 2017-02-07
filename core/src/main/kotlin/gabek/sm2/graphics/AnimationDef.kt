package gabek.sm2.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion
import gabek.sm2.Assets

/**
 * @author Gabriel Keith
 */
class AnimationDef(val delay: Float, val pingpong: Boolean, val repeats: Boolean) {
    val frames = mutableListOf<TextureRegion>()

    fun addFrames(vararg textures: TextureRegion) {
        frames.addAll(textures)
    }

    fun addFrames(assets: Assets, pack: String, region: String){
        frames.addAll(assets.findTextures(pack, region))
    }

    fun addFrame(assets: Assets, pack: String, region: String, index: Int){
        frames.add(assets.findTexture(pack, region, index))
    }
}
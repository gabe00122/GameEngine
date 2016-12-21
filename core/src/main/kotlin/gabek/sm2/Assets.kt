package gabek.sm2

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable

/**
 * @author Gabriel Keith
 */
class Assets : Disposable {
    private val resourceManager = AssetManager()
    private val packMap = mutableMapOf<String, TexturePack>()

    init{
        addPack("actors", "art/actors.atlas")
        addPack("tiles", "art/tiles.atlas")
    }

    fun finish(){
        resourceManager.finishLoading()

        val packIter = packMap.iterator()
        while (packIter.hasNext()){
            val entry = packIter.next()
            entry.value.atlas = resourceManager.get(entry.value.atlasName, TextureAtlas::class.java)
        }
    }

    fun addPack(packName: String, fileName: String){
        packMap[packName] = TexturePack(fileName)
        resourceManager.load(fileName, TextureAtlas::class.java)
    }

    fun findTexture(packName: String, regionName: String, index: Int = -1): TextureRegion{
        if(index == -1) {
            return packMap[packName]!!.atlas!!.findRegion(regionName)
        } else {
            return packMap[packName]!!.atlas!!.findRegion(regionName, index)
        }
    }

    override fun dispose(){
        resourceManager.dispose()
    }

    private class TexturePack(val atlasName: String, var atlas: TextureAtlas? = null)
}
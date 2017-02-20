package gabek.sm2

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable
import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
class Assets : Disposable {
    val resourceManager = AssetManager()
    private val packs = mutableListOf<TexturePack>()

    private val textureIdMap = mutableMapOf<String, Int>()
    private val textureList = mutableListOf<TextureRegion>()

    init {
        addPack("actors", "art/actors.atlas")
        addPack("tiles", "art/tiles.atlas")
        addPack("menus", "art/menus.atlas")

        //resourceManager.load("shaders/hex.vert", ShaderProgram::class.java)
    }

    fun finish() {
        resourceManager.finishLoading()

        for(pack in packs){
            val atlas  = resourceManager.get(pack.atlasName, TextureAtlas::class.java)

            for(region in atlas.regions){
                val id = textureList.size
                textureList.add(region)
                textureIdMap.put("${pack.packName}:${region.name}:${region.index}", id)
            }
        }
    }

    fun addPack(packName: String, fileName: String) {
        packs.add(TexturePack(packName, fileName))
        resourceManager.load(fileName, TextureAtlas::class.java)
    }

    fun findTexture(lookup: String): TextureRef {
        return TextureRef(textureIdMap[assumeIndex(lookup)]!!)
    }

    fun findTextures(lookup: String, range: IntRange): List<TextureRef>{
        return range.map { findTexture("$lookup:$it") }
    }

    fun retrieveRegion(ref: TextureRef): TextureRegion{
        return textureList[ref.id]
    }

    fun retrieveRegion(lookup: String): TextureRegion{
        return textureList[textureIdMap[assumeIndex(lookup)]!!]
    }

    fun assumeIndex(lookup: String): String{
        if(lookup.count { it == ':' } == 2){
            return lookup
        } else {
            return "$lookup:-1"
        }
    }

    override fun dispose() {
        resourceManager.dispose()
    }

    private class TexturePack(val packName: String, val atlasName: String)
}
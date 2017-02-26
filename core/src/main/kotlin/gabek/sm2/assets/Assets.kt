package gabek.sm2.assets

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Logger
import gabek.sm2.graphics.AnimationDef
import gabek.sm2.graphics.TextureRef

/**
 * @author Gabriel Keith
 */
class Assets() : Disposable {
    private val resourceManager = AssetManager()

    private val textureIdMap = mutableMapOf<String, Int>()
    private val textureList = mutableListOf<TextureRegion>()

    private val shaderMap = mutableMapOf<String, ShaderProgram>()
    private val animationMap = mutableMapOf<String, AnimationDef>()

    constructor(jsonFile: String, configOnly: Boolean = true) : this() {
        initLoaders()

        configureFromFile(jsonFile)
        if(!configOnly){
            finish()
        }
    }

    fun finish() {
        resourceManager.logger.level = Logger.INFO
        resourceManager.finishLoading()
    }

    private fun initLoaders(){
        resourceManager.setLoader(AnimationMap::class.java, AnimationLoader(this, resourceManager.fileHandleResolver))
    }

    fun configureFromFile(jsonFile: String){
        JsonAssetLoader(resourceManager.fileHandleResolver.resolve(jsonFile)).configure(this)
    }

    fun addTexturePack(packName: String, fileName: String) {
        val parameter = TextureAtlasLoader.TextureAtlasParameter()
        parameter.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, type ->
            val atlas = assetManager.get(fileName, TextureAtlas::class.java)

            for(region in atlas.regions){
                val id = textureList.size
                textureList.add(region)
                textureIdMap.put("$packName:${region.name}:${region.index}", id)
            }
        }

        resourceManager.load(fileName, TextureAtlas::class.java, parameter)
    }

    fun addShader(shaderName: String, vertFile: String, fragFile: String){
        val parameter = ShaderProgramLoader.ShaderProgramParameter()
        parameter.vertexFile = vertFile
        parameter.fragmentFile = fragFile

        parameter.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, type ->
            val shaderProgram = assetManager.get(fileName, ShaderProgram::class.java)
            shaderMap.put(shaderName, shaderProgram)
        }

        resourceManager.load(shaderName, ShaderProgram::class.java, parameter)
    }

    fun addAnimationPack(packName: String, file: String){
        val param = AnimationLoader.Parameters()

        param.loadedCallback = AssetLoaderParameters.LoadedCallback { assetManager, fileName, type ->
            val aMap = assetManager.get(file, AnimationMap::class.java)
            for(anim in aMap){
                animationMap.put("$packName:${anim.key}", anim.value)
            }
        }

        resourceManager.load(file, AnimationMap::class.java, param)
    }

    fun findTexture(lookup: String): TextureRef {
        return TextureRef(textureIdMap[defaultIndex(lookup)]!!)
    }

    fun findTextures(lookup: String, range: IntRange): List<TextureRef>{
        return range.map { findTexture("$lookup:$it") }
    }

    fun findTextures(lookup: String): List<TextureRef>{
        val base = lookup.replace(":*", "")
        val out = mutableListOf<TextureRef>()

        var index = 0
        var current = textureIdMap["$base:${index++}"]

        while(current != null){
            out.add(TextureRef(current))
            current = textureIdMap["$base:${index++}"]
        }

        return out
    }

    private fun defaultIndex(lookup: String): String{
        if(lookup.count { it == ':' } < 2){
            return "$lookup:-1"
        } else {
            return lookup
        }
    }

    fun retrieveRegion(ref: TextureRef): TextureRegion {
        return textureList[ref.id]
    }

    fun retrieveRegion(lookup: String): TextureRegion {
        return textureList[textureIdMap[defaultIndex(lookup)]!!]
    }

    fun retrieveAnimationDef(lookup: String): AnimationDef{
        return animationMap[lookup]!!
    }

    override fun dispose() {
        resourceManager.dispose()
    }
}
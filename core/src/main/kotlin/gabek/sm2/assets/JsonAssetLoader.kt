package gabek.sm2.assets

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * @author Gabriel Keith
 */
class JsonAssetLoader(val handle: FileHandle){
    companion object{
        private const val TEXTURE_PACK_LABEL = "texturePacks"

        private const val SHADER_LABEL = "shaders"
        private const val SHADER_VERT_LABEL = "vert"
        private const val SHADER_FRAG_LABEL = "frag"
    }

    private val jsonReader = JsonReader()

    fun configure(assets: Assets){
        val json = jsonReader.parse(handle)

        configTexturePacks(assets, json)
        configShaders(assets, json)
    }

    private fun configTexturePacks(assets: Assets, json: JsonValue){
        val root = json.get(TEXTURE_PACK_LABEL)

        for(pack in root.JsonIterator()){
            assert(pack.isString)
            assets.addTexturePack(pack.name, pack.asString())
        }
    }

    private fun configShaders(assets: Assets, json: JsonValue){
        val root = json.get(SHADER_LABEL)

        for(shader in root.JsonIterator()){
            assert(shader.has(SHADER_VERT_LABEL))
            assert(shader.get(SHADER_VERT_LABEL).isString)
            assert(shader.has(SHADER_FRAG_LABEL))
            assert(shader.get(SHADER_FRAG_LABEL).isString)

            val vert = shader.get(SHADER_VERT_LABEL).asString()
            val frag = shader.get(SHADER_FRAG_LABEL).asString()

            assets.addShader(shader.name, vert, frag)
        }
    }
}
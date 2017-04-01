package gabek.sm2.assets

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

/**
 * @author Gabriel Keith
 */
class JsonAssetLoader(val handle: FileHandle) {
    companion object {
        private const val TEXTURE_PACK_LABEL = "texturePacks"

        private const val SHADER_LABEL = "shaders"
        private const val SHADER_VERT_LABEL = "vert"
        private const val SHADER_FRAG_LABEL = "frag"

        private const val ANIMATION_LABEL = "animations"

        private const val MUSIC_LABLE = "music"
    }

    private val rootDir = handle.parent()
    private val jsonReader = JsonReader()

    fun configure(assets: Assets) {
        val json = jsonReader.parse(handle)

        configTexturePacks(assets, json)
        configShaders(assets, json)
        configAnimations(assets, json)
        configMusic(assets, json)
    }

    private fun withRoot(path: String) = rootDir.child(path).path()

    private fun configTexturePacks(assets: Assets, json: JsonValue) {
        val root = json.get(TEXTURE_PACK_LABEL)

        for (pack in root.JsonIterator()) {
            assert(pack.isString)
            assets.addTexturePack(pack.name, withRoot(pack.asString()))
        }
    }

    private fun configShaders(assets: Assets, json: JsonValue) {
        val root = json.get(SHADER_LABEL)

        for (shader in root.JsonIterator()) {
            assert(shader.has(SHADER_VERT_LABEL))
            assert(shader.get(SHADER_VERT_LABEL).isString)
            assert(shader.has(SHADER_FRAG_LABEL))
            assert(shader.get(SHADER_FRAG_LABEL).isString)

            val vert = withRoot(shader.get(SHADER_VERT_LABEL).asString())
            val frag = withRoot(shader.get(SHADER_FRAG_LABEL).asString())

            assets.addShader(shader.name, vert, frag)
        }
    }

    private fun configAnimations(assets: Assets, json: JsonValue) {
        val root = json.get(ANIMATION_LABEL)

        for (animFile in root.JsonIterator()) {
            assets.addAnimationPack(animFile.name, withRoot(animFile.asString()))
        }
    }

    private fun configMusic(assets: Assets, json: JsonValue){
        val root = json.get(MUSIC_LABLE)

        for(music in root.JsonIterator()){
            assets.addMusic(music.name, withRoot(music.asString()))
        }
    }
}
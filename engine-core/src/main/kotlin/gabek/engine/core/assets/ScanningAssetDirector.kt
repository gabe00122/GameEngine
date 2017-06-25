package gabek.engine.core.assets

import com.badlogic.gdx.files.FileHandle
import java.io.FileFilter
import java.lang.IllegalStateException

/**
 * @author Gabriel Keith
 * @date 5/31/2017
 */

class ScanningAssetDirector(val root: FileHandle){
    private val textureDir = "texture"
    private val animationDir = "animation"
    private val musicDir = "music"
    private val soundDir = "sound"
    private val shaderDir = "shader"

    fun configure(assets: Assets){
        loadTextures(assets)
        loadAnimations(assets)
        loadMusic(assets)
        loadSound(assets)
        loadShader(assets)
    }


    private fun loadTextures(assets: Assets) {
        val dir = root.child(textureDir)

        if(dir.exists()) {
            for (file in dir.list()) {
                if(dir.isDirectory) {
                    assets.loadTexturePack(file.nameWithoutExtension(), file.path())
                }
            }
        }
    }

    private fun loadAnimations(assets: Assets){
        val dir = root.child(animationDir)

        if(dir.exists()) {
            for(file in dir.list(".json")) {
                assets.loadAnimationPack(file.nameWithoutExtension(), file.path())
            }
        }
    }

    private fun loadMusic(assets: Assets){
        val dir = root.child(musicDir)

        if(dir.exists()) {
            for(file in dir.list(".ogg")) {
                assets.loadMusic(file.nameWithoutExtension(), file.path())
            }
        }
    }

    private fun loadSound(assets: Assets){
        val dir = root.child(soundDir)

        if(dir.exists()) {
            for(file in dir.list(".wav")) {
                assets.loadSound(file.nameWithoutExtension(), file.path())
            }
        }
    }

    private fun loadShader(assets: Assets){
        val dir = root.child(shaderDir)

        if(dir.exists()) {
            for(file in dir.list(".vert")) {
                val fragFile = dir.child(dir.nameWithoutExtension() + ".frag")

                if(!fragFile.exists())
                    throw IllegalStateException("Vertex file ${file.name()} dose not have a corresponding fragment.")

                assets.loadShader(file.nameWithoutExtension(), file.path(), fragFile.path())
            }
        }
    }
}
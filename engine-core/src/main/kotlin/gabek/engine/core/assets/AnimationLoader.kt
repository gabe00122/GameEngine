package gabek.engine.core.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.JsonReader
import gabek.engine.core.graphics.AnimationRef
import gabek.engine.core.graphics.AnimationRef.Companion.builder

/**
 * @author Gabriel Keith
 */
class AnimationLoader(val assets: Assets, fileHandleResolver: FileHandleResolver) :
        SynchronousAssetLoader<AnimationMap, AnimationLoader.Parameters>(fileHandleResolver) {

    private val jsonReader = JsonReader()

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: Parameters?): Array<AssetDescriptor<Any>>? {


        return null
    }

    override fun load(assetManager: AssetManager, fileName: String, file: FileHandle, parameter: Parameters): AnimationMap {
        val out = AnimationMap()

        val root = jsonReader.parse(file)

        val builder = builder(assets)
        for (animationDescription in root.JsonIterator()) {
            builder.strategy = AnimationRef.Strategy.valueOf(animationDescription.getString("strategy"))
            builder.delay = animationDescription.getFloat("delay", -1f)
            builder.addFrame(animationDescription.getString("frames"))

            out.put(animationDescription.name, builder.build())
        }

        return out
    }

    class Parameters : AssetLoaderParameters<AnimationMap>()
}
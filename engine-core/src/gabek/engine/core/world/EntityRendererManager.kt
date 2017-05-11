package gabek.engine.core.world

import com.artemis.utils.IntBag
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

/**
 * @author Gabriel Keith
 * @date 4/7/2017
 */

class EntityRendererManager(val renderSystems: List<Renderer>): RenderManager.BatchSystem {
    val layerSize = 10
    val layers = Array(layerSize) { Layer() }

    override fun render(batch: SpriteBatch, culling: Rectangle, progress: Float) {
        for(rendererIndex in 0 until renderSystems.size){
            renderSystems[rendererIndex].fill(layers, culling, progress)

            for(layer in layers){
                layer.pushRenderer(rendererIndex)
            }
        }

        for(layer in layers){
            layer.render(batch, progress)
            layer.clear()
        }
    }

    interface Renderer {
        fun fill(layers: Array<Layer>, culling: Rectangle, progress: Float)
        fun render(entity: Int, batch: SpriteBatch, progress: Float)
    }

    inner class Layer {
        private val indices = IntArray(renderSystems.size)
        private val renderIds = IntBag()

        fun pushIndex(id: Int){
            renderIds.add(id)
        }

        internal fun pushRenderer(rendererIndex: Int){
            indices[rendererIndex] = renderIds.size()
        }

        internal fun render(batch: SpriteBatch, progress: Float) {
            var i = 0

            for(rendererIndex in 0 until indices.size){
                val renderer = renderSystems[rendererIndex]
                val rendererBound = indices[rendererIndex]
                while(i < rendererBound){
                    renderer.render(renderIds[i++], batch, progress)
                }
            }
        }

        internal fun clear(){
            renderIds.setSize(0)
        }
    }
}
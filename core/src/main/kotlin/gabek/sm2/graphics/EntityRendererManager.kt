package gabek.sm2.graphics

import com.artemis.utils.IntBag
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import gabek.sm2.graphics.RenderManager

/**
 * @author Gabriel Keith
 * @date 4/7/2017
 */

class EntityRendererManager(renderSystems: List<Renderer>): RenderManager.BatchSystem {
    val layerSize = 10
    private val stages = ArrayList<RenderStage>()

    init {
        renderSystems.mapTo(stages) { RenderStage(it) }
    }

    override fun render(batch: SpriteBatch, culling: Rectangle, progress: Float) {
        for(stage in stages){
            stage.fill(culling, progress)
        }

        for(layer in 0 until 10){
            for(state in stages){
                state.renderLayer(layer, batch, culling, progress)
            }
        }
    }


    interface Renderer {
        fun fill(state: Array<IntBag>, culling: Rectangle, progress: Float)
        fun render(entity: Int, batch: SpriteBatch, culling: Rectangle, progress: Float)
    }

    private inner class RenderStage(val renderer: Renderer){
        val data = Array(layerSize, { IntBag() })
        fun clear(){
            for(d in data){
                d.clear()
            }
        }

        fun fill(culling: Rectangle, progress: Float){
            clear()
            renderer.fill(data, culling, progress)
        }

        fun renderLayer(layer: Int, batch: SpriteBatch, culling: Rectangle, progress: Float){
            val bag = data[layer]
            for(i in 0 until bag.size()){
                renderer.render(bag[i], batch, culling, progress)
            }
        }
    }
}
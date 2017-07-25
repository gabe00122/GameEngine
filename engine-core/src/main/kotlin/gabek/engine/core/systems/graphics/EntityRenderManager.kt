package gabek.engine.core.systems.graphics

import com.artemis.utils.IntBag
import com.badlogic.gdx.graphics.g2d.Batch
import gabek.engine.core.graphics.RenderContext

/**
 * @author Gabriel Keith
 * @date 4/7/2017
 */

class EntityRenderManager(vararg val renderSystems: EntityRenderSystem): RenderManager.RenderSystem {
    val layerSize = 10
    private val layers = Array(layerSize) { Layer() }
    private val schedule = RenderSchedule()

    override fun render(batch: Batch, context: RenderContext) {
        for(rendererIndex in 0 until renderSystems.size){
            renderSystems[rendererIndex].fill(schedule, context)

            for(layer in layers){
                layer.pushRenderer(rendererIndex)
            }
        }

        for(layer in layers){
            layer.render(batch, context)
            layer.clear()
        }
    }

    interface EntityRenderSystem {
        fun fill(schedule: RenderSchedule, context: RenderContext)
        fun render(entity: Int, batch: Batch, context: RenderContext)
    }

    inner class RenderSchedule {
        fun pushId(renderId: Int, layerIndex: Int){
            layers[layerIndex].renderIds.add(renderId)
        }
    }

    private inner class Layer {
        val indices = IntArray(renderSystems.size)
        val renderIds = IntBag()

        internal fun pushRenderer(rendererIndex: Int){
            indices[rendererIndex] = renderIds.size()
        }

        internal fun render(batch: Batch, context: RenderContext) {
            var i = 0

            for(rendererIndex in 0 until indices.size){
                val renderer = renderSystems[rendererIndex]
                val rendererBound = indices[rendererIndex]
                while(i < rendererBound){
                    renderer.render(renderIds[i++], batch, context)
                }
            }
        }

        internal fun clear(){
            renderIds.setSize(0)
        }
    }
}
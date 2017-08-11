package gabek.engine.core.tilemap

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import gabek.engine.core.graphics.RenderContext
import gabek.engine.core.systems.graphics.RenderManager


/**
 * @author Gabriel Keith
 * @date 7/27/2017
 */
open class TileMapLayer(val definitions: TileDefinitions, val tileSize: Float, val pixelToMeters: Float)
    : RenderManager.RenderSystem{

    val grid: Grid<TileInstance?> = ArrayGrid{ _, _ -> null}

    var listener: TileChangeListener? = null

    open fun set(x: Int, y: Int, value: TileInstance) {
        grid.set(x, y, value)
        definitions[value].onTileInit(x, y, value)

        listener?.onTileSet(x, y, value)
    }

    fun resize(offsetX: Int, offsetY: Int, width: Int, height: Int){
        grid.resize(offsetX, offsetY, width, height)
    }

    override fun render(batch: Batch, context: RenderContext) {
        val culling = context.culling

        val x1 = MathUtils.clamp(MathUtils.floor(culling.x / tileSize), 0, grid.width)
        val x2 = MathUtils.clamp(MathUtils.ceil((culling.x + culling.width) / tileSize), 0, grid.width)

        val y1 = MathUtils.clamp(MathUtils.floor(culling.y / tileSize), 0, grid.height)
        val y2 = MathUtils.clamp(MathUtils.ceil((culling.y + culling.height) / tileSize), 0, grid.height)

        for (y in y1 until y2) {
            for (x in x1 until x2) {
                drawTile(batch, context, pixelToMeters, x, y)
            }
        }
    }

    private fun drawTile(batch: Batch, context: RenderContext, pixelToMeters: Float ,x: Int, y: Int) {
        val tile = grid.get(x, y)
        if(tile != null) {
            val ref = definitions[tile.typeId].texture
            if (ref != null) {
                val refW = pixelToMeters * ref.texture.regionWidth
                val refH = pixelToMeters * ref.texture.regionHeight

                val worldX = (x * tileSize + tileSize / 2f + ref.offsetX * pixelToMeters) - refW / 2f
                val worldY = (y * tileSize + tileSize / 2f + ref.offsetY * pixelToMeters) - refH / 2f

                batch.draw(ref.texture, worldX, worldY, refW, refH)
            }
        }
    }

    interface TileChangeListener{
        fun onTileSet(x: Int, y: Int, value: TileInstance)
    }
}
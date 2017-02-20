package gabek.sm2.systems.graphics

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import gabek.sm2.systems.TileMapSystem
import gabek.sm2.world.RenderManager

/**
 * @author Gabriel Keith
 */
class TileRenderSystem: BaseSystem(), RenderManager.BatchSystem{
    private lateinit var tileSystem: TileMapSystem

    override fun processSystem() {}
    override fun render(batch: SpriteBatch, culling: Rectangle, progress: Float) {
        tileSystem.tileMap.render(batch, culling)
    }
}
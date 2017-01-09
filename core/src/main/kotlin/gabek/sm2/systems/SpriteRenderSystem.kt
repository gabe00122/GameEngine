package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import gabek.sm2.components.SpriteCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class SpriteRenderSystem : BaseEntitySystem(Aspect.all(SpriteCom::class.java, TranslationCom::class.java)) {
    private lateinit var tileMapSystem: TileMapSystem
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    private val temp = Rectangle()

    override fun processSystem() {}

    fun render(batch: SpriteBatch, culling: Rectangle, progress: Float) {
        //render tiles
        tileMapSystem.tileMap.render(batch, culling)

        //render entity
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val spriteComp = spriteMapper.get(entity)
            val transComp = translationMapper.get(entity)

            val region = spriteComp.texture
            if (region != null) {
                val x = transComp.lerpX(progress)
                val y = transComp.lerpY(progress)
                val width = spriteComp.width
                val height = spriteComp.height
                val halfWidth = width / 2f
                val halfHeight = height / 2f
                val rotation = transComp.lerpRotation(progress)


                val size = Math.sqrt((width * width + height * height).toDouble()).toFloat()
                temp.set(x - size/2f + spriteComp.offsetX, y - size/2f + spriteComp.offsetY, size, size)


                if(culling.overlaps(temp)) {
                    val flipX = spriteComp.flipX
                    val flipY = spriteComp.flipY

                    region.flip(flipX, flipY)
                    batch.draw(spriteComp.texture,
                            x - halfWidth + spriteComp.offsetX, y - halfHeight + spriteComp.offsetY,
                            halfWidth, halfHeight, width, height, 1f, 1f, rotation)
                    region.flip(flipX, flipY)
                }
            }
        }
    }
}

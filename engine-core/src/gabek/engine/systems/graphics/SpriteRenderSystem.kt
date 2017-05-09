package gabek.engine.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.components.common.TranslationCom
import gabek.engine.components.graphics.SpriteCom
import gabek.engine.world.EntityRendererManager

/**
 * @author Gabriel Keith
 */
class SpriteRenderSystem(kodein: Kodein)
    : BaseEntitySystem(Aspect.all(SpriteCom::class.java, TranslationCom::class.java))
        , EntityRendererManager.Renderer {

    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    private val temp = Rectangle()

    override fun fill(layers: Array<EntityRendererManager.Layer>, culling: Rectangle, progress: Float) {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val spriteComp = spriteMapper.get(entity)
            val transComp = translationMapper.get(entity)

            if (spriteComp.textureRef != null) {
                val x = transComp.lerpX(progress) + spriteComp.offsetX
                val y = transComp.lerpY(progress) + spriteComp.offsetY
                val width = spriteComp.width
                val height = spriteComp.height
                val rotation = transComp.lerpRotation(progress) + spriteComp.offsetRotation

                val r = rotation + spriteComp.offsetRotation
                val sin = MathUtils.sinDeg(r)
                val asin = if (sin > 0) sin else -sin
                val cos = MathUtils.cosDeg(r)
                val acos = if (cos > 0) cos else -cos

                temp.width = height * asin + width * acos
                temp.height = width * asin + height * acos
                temp.x = x - temp.width / 2f
                temp.y = y - temp.height / 2f

                if (culling.overlaps(temp)) {
                    layers[spriteComp.layer].pushIndex(entity)
                }
            }
        }
    }

    override fun render(entity: Int, batch: SpriteBatch, progress: Float) {
        val spriteComp = spriteMapper.get(entity)
        val transComp = translationMapper.get(entity)

        val ref = spriteComp.textureRef

        if (ref != null) {
            val region = ref.texture

            val x = transComp.lerpX(progress) + spriteComp.offsetX
            val y = transComp.lerpY(progress) + spriteComp.offsetY
            val width = spriteComp.width
            val height = spriteComp.height
            val halfWidth = width / 2f
            val halfHeight = height / 2f
            val rotation = transComp.lerpRotation(progress) + spriteComp.offsetRotation

            val flipX = spriteComp.flipX
            val flipY = spriteComp.flipY

            val oldColor = batch.color
            batch.color = spriteComp.tint

            region.flip(flipX, flipY)
            batch.draw(region, x - halfWidth, y - halfHeight,
                    halfWidth, halfHeight, width, height, 1f, 1f, rotation)
            region.flip(flipX, flipY)
            batch.color = oldColor
        }
    }

    override fun processSystem() {}
}

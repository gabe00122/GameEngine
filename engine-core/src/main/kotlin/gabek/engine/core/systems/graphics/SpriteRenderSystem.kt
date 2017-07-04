package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.graphics.RenderContext
import gabek.engine.core.world.EntityRenderManager

/**
 * @author Gabriel Keith
 */
class SpriteRenderSystem(kodein: Kodein): BaseEntitySystem(
        Aspect.all(
                SpriteCom::class.java,
                SizeCom::class.java,
                TranslationCom::class.java
        )
), EntityRenderManager.EntityRenderSystem {

    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var sizeMapper: ComponentMapper<SizeCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    private lateinit var cullingSystem: CullingSystem

    private val pixelRatio: PixelRatio = kodein.instance<PixelRatio>()

    override fun fill(schedule: EntityRenderManager.RenderSchedule, context: RenderContext) {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val spriteComp = spriteMapper[entity]

            val ref = spriteComp.textureRef
            if (ref != null) {
                val transComp = translationMapper[entity]

                val pixelToMeter = pixelRatio.pixelToMeters

                val progress = context.progress
                if(cullingSystem.cull(context.culling,
                        x = transComp.lerpX(progress) + spriteComp.offsetX + (ref.offsetX * pixelToMeter),
                        y = transComp.lerpY(progress) + spriteComp.offsetY + (ref.offsetY * pixelToMeter),
                        rotation = transComp.lerpRotation(progress),
                        width = ref.texture.regionWidth * pixelToMeter,
                        height = ref.texture.regionHeight * pixelToMeter
                )) {
                    schedule.pushId(entity, spriteComp.layer)
                }
            }
        }
    }

    override fun render(entity: Int, batch: SpriteBatch, context: RenderContext) {
        val spriteComp = spriteMapper[entity]
        val sizeComp = sizeMapper[entity]
        val transComp = translationMapper[entity]

        val ref = spriteComp.textureRef

        if (ref != null) {
            val pixelToMeter = pixelRatio.pixelToMeters

            val region = ref.texture

            val progress = context.progress

            val x = transComp.lerpX(progress) + spriteComp.offsetX + (ref.offsetX * pixelToMeter)
            val y = transComp.lerpY(progress) + spriteComp.offsetY + (ref.offsetY * pixelToMeter)
            val width = region.regionWidth * pixelToMeter
            val height = region.regionHeight * pixelToMeter
            val halfWidth = width / 2f
            val halfHeight = height / 2f
            val rotation = transComp.lerpRotation(progress)// + spriteComp.offsetRotation

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

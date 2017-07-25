package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.AnimationCom
import gabek.engine.core.graphics.Animation
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.graphics.RenderContext
import gabek.engine.core.graphics.Sprite
import gabek.engine.core.systems.common.UpdateManager

/**
 * @author Gabriel Keith
 */
class AnimationSystem(kodein: Kodein) : BaseEntitySystem(Aspect.all(AnimationCom::class.java)), EntityRenderManager.EntityRenderSystem {
    private lateinit var updateManager: UpdateManager
    private lateinit var animationMapper: ComponentMapper<AnimationCom>

    private lateinit var sizeMapper: ComponentMapper<SizeCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    private lateinit var cullingSystem: CullingSystem

    private val pixelRatio: PixelRatio = kodein.instance<PixelRatio>()

    override fun processSystem() {

    }

    override fun inserted(entityId: Int) {
        super.inserted(entityId)

        val animComp = animationMapper[entityId]
        animComp.startFrame = updateManager.currentFrame
    }

    fun beginAnimation(entity: Int, animation: Animation) {
        val animComp = animationMapper[entity]
        animComp.animation = animation
        animComp.startFrame = updateManager.currentFrame
    }

    private fun getSprite(animationCom: AnimationCom, progress: Float): Sprite?{
        val animation = animationCom.animation ?: return null

        return animation.getSprite(updateManager.getElapsedTime(animationCom.startFrame) + progress)
    }

    override fun fill(schedule: EntityRenderManager.RenderSchedule, context: RenderContext) {
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val animationCom = animationMapper[entity]

            val ref = getSprite(animationCom, context.progress)
            if (ref != null) {
                val transComp = translationMapper[entity]

                val pixelToMeter = pixelRatio.pixelToMeters

                val progress = context.progress
                if(cullingSystem.cull(context.culling,
                        x = transComp.lerpX(progress) + (ref.offsetX * pixelToMeter),
                        y = transComp.lerpY(progress) + (ref.offsetY * pixelToMeter),
                        rotation = transComp.lerpRotation(progress),
                        width = ref.texture.regionWidth * pixelToMeter,
                        height = ref.texture.regionHeight * pixelToMeter
                )) {
                    schedule.pushId(entity, 2) //TODO fix this
                }
            }
        }
    }

    override fun render(entity: Int, batch: Batch, context: RenderContext) {
        val animationCom = animationMapper[entity]
        val transComp = translationMapper[entity]

        val ref = getSprite(animationCom, context.progress)

        if (ref != null) {
            val pixelToMeter = pixelRatio.pixelToMeters

            val region = ref.texture

            val progress = context.progress

            val x = transComp.lerpX(progress) + (ref.offsetX * pixelToMeter)
            val y = transComp.lerpY(progress)  + (ref.offsetY * pixelToMeter)
            val width = region.regionWidth * pixelToMeter
            val height = region.regionHeight * pixelToMeter
            val halfWidth = width / 2f
            val halfHeight = height / 2f
            val rotation = transComp.lerpRotation(progress)// + spriteComp.offsetRotation

            //val flipX = animationCom.flipX
            //val flipY = animationCom.flipY

            val oldColor = batch.color
            //batch.color = animationCom.tint

            //region.flip(flipX, flipY)
            batch.draw(region, x - halfWidth, y - halfHeight,
                    halfWidth, halfHeight, width, height, 1f, 1f, rotation)

            //region.flip(flipX, flipY)
            batch.color = oldColor
        }
    }
}
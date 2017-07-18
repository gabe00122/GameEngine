package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.graphics.ParallaxGraphicCom
import gabek.engine.core.graphics.RenderContext

/**
 * @author Gabriel Keith
 * @date 4/15/2017
 */
class ParallaxRenderSystem(kodein: Kodein): BaseEntitySystem(Aspect.all(ParallaxGraphicCom::class.java)),
        RenderManager.RenderSystem {
    private val assets: Assets = kodein.instance()
    private lateinit var parallaxMapper: ComponentMapper<ParallaxGraphicCom>


    override fun processSystem() {}

    override fun render(batch: Batch, context: RenderContext) {
        val culling = context.culling
        val centerX = culling.x - culling.width / 2f
        val centerY = culling.y - culling.height / 2f

        val entities = entityIds

        //batch.disableBlending()

        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val parallax = parallaxMapper[entity]
            val region = parallax.sprite

            if (region != null) {
                batch.draw(region.texture,
                        centerX * parallax.scrollFactorX,
                        0f,
                        1920 / 60f,
                        1080 / 60f)
            }
        }

        //batch.enableBlending()
    }

    fun createParallax(spriteName: String, scrollX: Float, scrollY: Float) {
        val entity = world.create()
        val par = parallaxMapper.create(entity)

        par.sprite = assets.getTexture(spriteName)
        par.scrollFactorX = scrollX
        par.scrollFactorY = scrollY
    }
}
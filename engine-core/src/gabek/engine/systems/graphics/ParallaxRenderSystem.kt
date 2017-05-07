package gabek.sm2.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.graphics.ParallaxGraphicCom
import gabek.sm2.world.RenderManager

/**
 * @author Gabriel Keith
 * @date 4/15/2017
 */
class ParallaxRenderSystem(kodein: Kodein) : BaseEntitySystem(Aspect.all(ParallaxGraphicCom::class.java)),
        RenderManager.BatchSystem {
    private val assets: Assets = kodein.instance()
    private lateinit var parallaxMapper: ComponentMapper<ParallaxGraphicCom>


    override fun processSystem() {}

    override fun render(batch: SpriteBatch, culling: Rectangle, progress: Float) {
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

        par.sprite = assets.findTexture(spriteName)
        par.scrollFactorX = scrollX
        par.scrollFactorY = scrollY
    }
}
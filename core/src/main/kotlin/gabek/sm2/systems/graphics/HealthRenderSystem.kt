package gabek.sm2.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.character.HealthCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.graphics.HealthDisplayCom
import gabek.sm2.world.RenderManager

/**
 * @author Gabriel Keith
 */
class HealthRenderSystem(kodein: Kodein) : BaseEntitySystem(
        Aspect.all(
                HealthCom::class.java,
                HealthDisplayCom::class.java,
                TranslationCom::class.java)
), RenderManager.BatchSystem{
    private val assets: Assets = kodein.instance()

    private val rect = assets.retrieveRegion("actors:rect")

    private val background = Color(89/255f, 86/255f, 82/255f, 1f)
    private val foreground = Color(106/255f, 190/255f, 48/255f, 1f)

    private val temp = Rectangle()

    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var healthMapper: ComponentMapper<HealthCom>
    private lateinit var healthDisplayMapper: ComponentMapper<HealthDisplayCom>

    override fun processSystem() {}
    override fun render(batch: SpriteBatch, culling: Rectangle, progress: Float){
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val trans = transMapper[entity]
            val health = healthMapper[entity]
            val healthDisplay = healthDisplayMapper[entity]

            temp.width = 1f
            temp.height = 0.2f

            temp.x = trans.lerpX(progress) + healthDisplay.offsetX - temp.width / 2f
            temp.y = trans.lerpY(progress) + healthDisplay.offsetY - temp.height / 2f

            if(culling.overlaps(temp)) {
                batch.color = background
                batch.draw(rect, temp.x, temp.y, temp.width, temp.height)

                batch.color = foreground
                batch.draw(rect, temp.x, temp.y, temp.width * (health.healthPoints / health.maximumHealth.toFloat()), temp.height)

                batch.color = Color.WHITE
            }
        }
    }
}
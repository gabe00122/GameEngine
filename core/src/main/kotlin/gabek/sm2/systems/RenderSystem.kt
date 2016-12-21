package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import gabek.sm2.components.SpriteCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class RenderSystem : BaseEntitySystem(Aspect.all(SpriteCom::class.java, TranslationCom::class.java)){
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    override fun processSystem() {}

    fun draw(batch: SpriteBatch, progress: Float){
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]
            val spriteComp = spriteMapper.get(entity)
            val transComp = translationMapper.get(entity)

            //batch.draw(spriteComp.texture, transComp.lerpX(progress), transComp.lerpY(progress), 1f, 1f)
            batch.draw(spriteComp.texture,
                    transComp.lerpX(progress) - spriteComp.width/2,
                    transComp.lerpY(progress) - spriteComp.height/2,
                    spriteComp.width/2, spriteComp.height/2,
                    spriteComp.width, spriteComp.height,
                    1f, 1f, transComp.rotation)
        }
    }
}

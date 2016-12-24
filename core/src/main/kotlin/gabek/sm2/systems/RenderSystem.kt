package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Affine2
import com.badlogic.gdx.math.MathUtils
import gabek.sm2.components.SpriteCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class RenderSystem : BaseEntitySystem(Aspect.all(SpriteCom::class.java, TranslationCom::class.java)){
    private lateinit var tileMapSystem: TileMapSystem
    private lateinit var spriteMapper: ComponentMapper<SpriteCom>
    private lateinit var translationMapper: ComponentMapper<TranslationCom>

    override fun processSystem() {}

    fun draw(batch: SpriteBatch, progress: Float){
        //draw tiles
        tileMapSystem.tileMap.render(batch)

        //draw entity
        val entities = entityIds
        for(i in 0 until entities.size()){
            val entity = entities[i]
            val spriteComp = spriteMapper.get(entity)
            val transComp = translationMapper.get(entity)

            val region = spriteComp.texture
            if(region != null) {
                val flipX = spriteComp.flipX
                val flipY = spriteComp.flipY

                region.flip(flipX, flipY)
                batch.draw(spriteComp.texture,
                        transComp.lerpX(progress) - spriteComp.width / 2 + spriteComp.offsetX,
                        transComp.lerpY(progress) - spriteComp.height / 2 + spriteComp.offsetY,
                        spriteComp.width / 2, spriteComp.height / 2,
                        spriteComp.width, spriteComp.height,
                        1f, 1f, transComp.rotation)
                region.flip(flipX, flipY)
            }
        }
    }
}

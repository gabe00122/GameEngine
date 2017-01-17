package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.SpriteCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.physics.RCircle
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */
class PelletFactory(kodein: Kodein, val world: World): EntityFactory{
    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            SpriteCom::class.java,
            BodyCom::class.java
    ).build(world)
    
    private val assets: Assets = kodein.instance()
    private val texture: TextureRegion = assets.findTexture("actors", "rect")
    
    
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    
    fun create(x: Float, y: Float): Int{
        val id = world.create(arch)
        
        val trans = transMapper[id]
        val sprite = spriteMapper[id]
        val body = bodyMapper[id]
        
        trans.x = x
        trans.y = y
        trans.pX = x
        trans.pY = y
        
        sprite.texture = texture
        sprite.width = 0.2f
        sprite.height = 0.2f

        val fixture = RFixture()
        fixture.isSensor = true
        fixture.shape = RPolygon(0.2f, 0.2f)
        //fixture.density = 1000f

        body.rBody.bodyType = BodyDef.BodyType.KinematicBody
        body.rBody.isBullet = true
        body.rBody.x = x
        body.rBody.y = y

        body.rBody.linearVelocityX = 2f
        body.rBody.gravityScale = 0f
        body.rBody.addFixture(fixture)

        return id
    }
    
    override fun dispose() {

    }
}
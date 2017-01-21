package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.PelletLifeSpanCom
import gabek.sm2.components.graphics.SpriteCom
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
            BodyCom::class.java,
            PelletLifeSpanCom::class.java
    ).build(world)
    
    private val assets: Assets = kodein.instance()
    private val texture: TextureRegion = assets.findTexture("actors", "rect")
    
    
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val lifeSpanMapper = world.getMapper(PelletLifeSpanCom::class.java)

    fun create(x: Float, y: Float, direction: Float, speed: Float, lifeSpan: Float): Int{
        val id = world.create(arch)
        
        val trans = transMapper[id]
        val sprite = spriteMapper[id]
        val body = bodyMapper[id]
        val pelletLifeSpan = lifeSpanMapper[id]

        trans.initPos(x, y)
        
        sprite.texture = texture
        sprite.width = 0.2f
        sprite.height = 0.2f

        pelletLifeSpan.lifeSpan = lifeSpan

        val fixture = RFixture()
        //fixture.isSensor = true
        fixture.shape = RPolygon(0.2f, 0.2f)
        fixture.density = 10f
        fixture.restitution = 0.2f

        body.rBody.bodyType = BodyDef.BodyType.DynamicBody
        body.rBody.isBullet = true
        body.rBody.x = x
        body.rBody.y = y

        body.rBody.linearVelocityX = MathUtils.cosDeg(direction) * speed
        body.rBody.linearVelocityY = MathUtils.sinDeg(direction) * speed
        body.rBody.gravityScale = 0f
        body.rBody.addFixture(fixture)

        return id
    }
    
    override fun dispose() {

    }
}
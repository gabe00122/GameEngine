package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.ContactCom
import gabek.sm2.components.SpriteCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */
class JunkFactory(val kodein: Kodein, val world: World) {
    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            SpriteCom::class.java,
            BodyCom::class.java,
            ContactCom::class.java
    ).build(world)

    private val assets: Assets = kodein.instance()
    private val texture = assets.findTexture("actors", "smiles")

    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)

    fun create(x: Float, y: Float, w: Float, h: Float): Int {
        val id = world.create(arch)
        val trans = transMapper[id]
        val sprite = spriteMapper[id]
        val body = bodyMapper[id].rBody

        trans.x = x
        trans.y = y
        trans.pX = x
        trans.pY = y

        sprite.texture = texture
        sprite.width = w
        sprite.height = h

        val fixture = RFixture(RPolygon(w, h))
        fixture.friction = 0.5f
        fixture.density = 0.2f

        body.bodyType = BodyDef.BodyType.DynamicBody
        body.setPosition(x, y)
        body.addFixture(fixture)

        return id
    }
}
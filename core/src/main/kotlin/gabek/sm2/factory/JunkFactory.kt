package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.components.BodyCom
import gabek.sm2.components.ContactCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */
class JunkFactory(val kodein: Kodein, val world: World) {
    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            BodyCom::class.java,
            ContactCom::class.java
    ).build(world)

    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)

    fun create(x: Float, y: Float, w: Float, h: Float): Int{
        val id = world.create(arch)
        val trans = transMapper[id]
        val body = bodyMapper[id].body

        trans.x = x
        trans.y = y
        trans.pX = x
        trans.pY = y

        val fixture = RFixture(RPolygon(w, h))
        fixture.friction = 0.5f
        fixture.density = 1f

        body.colisionCallback = id
        body.bodyType = BodyDef.BodyType.DynamicBody
        body.setPosition(x, y)
        body.addFixture(fixture)

        return id
    }
}
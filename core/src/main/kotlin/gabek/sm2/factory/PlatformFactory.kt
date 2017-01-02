package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.components.BodyCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */
class PlatformFactory(kodein: Kodein, private val world: World) : Disposable {
    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java, BodyCom::class.java
    ).build(world)
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)

    fun create(x: Float, y: Float, w: Float, h: Float): Int {
        val id = world.create(arch)
        val trans = transMapper[id]
        val body = bodyMapper[id]

        trans.x = x
        trans.y = y
        trans.pX = x
        trans.pY = y

        val fixture = RFixture(RPolygon(w, h))
        fixture.friction = 1f

        body.rBody.addFixture(fixture)
        body.rBody.setPosition(x, y)

        return id
    }

    override fun dispose() {}
}
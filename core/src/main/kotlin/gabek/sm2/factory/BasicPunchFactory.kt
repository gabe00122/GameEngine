package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.ParentOfCom
import gabek.sm2.components.ParentOffsetCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.components.pellet.PelletCollisionCom
import gabek.sm2.components.pellet.PelletLifeSpanCom
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */

/*
class BasicPunchFactory(val kodein: Kodein, val world: World){
    val arch = ArchetypeBuilder().add(
            PelletLifeSpanCom::class.java,
            PelletCollisionCom::class.java,
            ParentOfCom::class.java,
            ParentOffsetCom::class.java,
            TranslationCom::class.java,
            BodyCom::class.java,
            SpriteCom::class.java
    ).build(world)
    private val lifeSpanMapper = world.getMapper(PelletLifeSpanCom::class.java)
    private val collisionMapper = world.getMapper(PelletCollisionCom::class.java)
    private val parentMapper = world.getMapper(ParentOfCom::class.java)
    private val offsetMapper = world.getMapper(ParentOffsetCom::class.java)
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)

    private val textureRef = kodein.instance<Assets>().findTexture("actors", "rect")

    fun create(parentId: Int, offsetX: Float, offsetY: Float, damage: Float): Int{
        val entity = world.create(arch)
        val lifeSpan = lifeSpanMapper[entity]
        val collision = collisionMapper[entity]
        val general = parentMapper[entity]
        val offset = offsetMapper[entity]
        val trans = transMapper[entity]
        val body = bodyMapper[entity].body
        val sprite = spriteMapper[entity]

        lifeSpan.lifeSpan = 0.25f

        collision.damage = damage
        collision.diesOnAttack = true

        general.general = parentId
        general.diesWithParent = true

        offset.x = offsetX
        offset.y = offsetY

        body.bodyType = BodyDef.BodyType.KinematicBody
        body.addFixture(RPolygon(0.25f, 0.25f), isSensor = true)

        sprite.textureRef = textureRef
        sprite.setSize(0.25f, 0.25f)

        if(parentId != -1 && bodyMapper.has(parentId)){
            val parentBody = bodyMapper[parentId].body
            trans.initPos(parentBody.x + offset.x, parentBody.y + offset.y)
            body.setPosition(trans.x, trans.y)
        }

        return entity
    }
}
*/
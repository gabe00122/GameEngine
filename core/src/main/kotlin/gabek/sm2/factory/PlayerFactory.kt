package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.*
import gabek.sm2.input.PlayerInput
import gabek.sm2.physics.RBody
import gabek.sm2.physics.RCircle
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon
import gabek.sm2.systems.Box2dSystem

/**
 * @author Gabriel Keith
 */
class PlayerFactory(kodein: Kodein, val world: World) : Disposable{
    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            SpriteCom::class.java,
            BodyCom::class.java,
            ContactCom::class.java,
            PlayerInputCom::class.java,
            CharacterLegsCom::class.java,
            CharacterControllerCom::class.java).build(world)

    private val assets: Assets = kodein.instance()
    private val texture = assets.findTexture("actors", "smiles")

    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val playerInputMapper = world.getMapper(PlayerInputCom::class.java)
    private val characterLegsMapper = world.getMapper(CharacterLegsCom::class.java)

    fun create(x: Float, y: Float, input: PlayerInput): Int{
        val id = world.create(arch)

        val trans = transMapper[id]
        val sprite = spriteMapper[id]
        val bodyComp = bodyMapper[id]
        val playerInput = playerInputMapper[id]
        val legs = characterLegsMapper[id]

        //set position
        trans.x = x
        trans.y = y
        trans.pX = x
        trans.pY = y

        //sprite
        sprite.texture = texture
        sprite.width = 0.5f
        sprite.height = 1.5f

        //body
        val fixture = RFixture(RPolygon(0.5f, 1.5f))
        fixture.friction = 0f
        fixture.density = 1f

        bodyComp.body.colisionCallback = id
        bodyComp.body.bodyType = BodyDef.BodyType.DynamicBody
        bodyComp.body.isFixedRotation = true
        bodyComp.body.setPosition(x, y)
        bodyComp.body.addFixture(fixture)

        //input
        playerInput.playerInput = input

        val wheel = RFixture(RCircle(0.25f))
        wheel.friction = 1f
        wheel.restitution = 0.0f
        wheel.density = 1f

        legs.body.colisionCallback = id
        legs.body.addFixture(wheel)
        legs.body.bodyType = BodyDef.BodyType.DynamicBody
        legs.body.setPosition(x, y - 0.75f)

        legs.motor.bodyA = bodyComp.body
        legs.motor.bodyB = legs.body
        legs.motor.isMoterEnabled = true
        legs.motor.motorSpeed = 180f
        legs.motor.maxTorque = 5f
        legs.motor.anchorAY = -0.75f

        return id
    }

    override fun dispose() {

    }
}
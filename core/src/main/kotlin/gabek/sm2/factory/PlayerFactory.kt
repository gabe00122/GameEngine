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
import gabek.sm2.graphics.Animation
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
    private val width = .5f
    private val height = 1f
    private val bodyHeight = height - width/2f

    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            AnimationCom::class.java,
            CharacterAnimatorCom::class.java,
            SpriteCom::class.java,
            BodyCom::class.java,
            ContactCom::class.java,
            PlayerInputCom::class.java,
            CharacterPeripheryCom::class.java,
            CharacterStateCom::class.java,
            CharacterControllerCom::class.java).build(world)

    private val assets: Assets = kodein.instance()
    private val runningAnimation = Animation(0.1f, true)
    private val stillAnimation = Animation(-1f, false)

    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val animatorMapper = world.getMapper(CharacterAnimatorCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val playerInputMapper = world.getMapper(PlayerInputCom::class.java)
    private val characterLegsMapper = world.getMapper(CharacterPeripheryCom::class.java)

    init{
        runningAnimation.frames.add(assets.findTexture("actors", "fred_1", 0))
        runningAnimation.frames.add(assets.findTexture("actors", "fred_1", 1))
        runningAnimation.frames.add(assets.findTexture("actors", "fred_1", 2))

        stillAnimation.frames.add(assets.findTexture("actors", "fred_1", 1))
    }

    fun create(x: Float, y: Float, input: PlayerInput): Int{
        val id = world.create(arch)

        val trans = transMapper[id]
        val animator = animatorMapper[id]
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
        animator.runningAnimation = runningAnimation
        animator.stillAnimation = stillAnimation

        sprite.width = width
        sprite.height = height
        sprite.offsetY = -width/4

        //body
        val fixture = RFixture(RPolygon(width, bodyHeight))
        fixture.friction = 1f
        fixture.density = 1f

        bodyComp.body.colisionCallback = id
        bodyComp.body.bodyType = BodyDef.BodyType.DynamicBody
        //bodyComp.body.linearDamping = 0.2f
        bodyComp.body.isFixedRotation = true
        bodyComp.body.setPosition(x, y)
        bodyComp.body.addFixture(fixture)

        //input
        playerInput.playerInput = input

        val wheel = RFixture(RCircle(width/2f))
        wheel.friction = 1f
        wheel.restitution = 0.0f
        wheel.density = 0.5f

        legs.body.colisionCallback = id
        legs.body.addFixture(wheel)
        legs.body.bodyType = BodyDef.BodyType.DynamicBody
        legs.body.setPosition(x, y - bodyHeight/2)

        legs.motor.bodyA = bodyComp.body
        legs.motor.bodyB = legs.body
        legs.motor.isMoterEnabled = true
        legs.motor.maxTorque = 5f
        legs.motor.anchorAY = -bodyHeight/2

        return id
    }

    override fun dispose() {

    }
}
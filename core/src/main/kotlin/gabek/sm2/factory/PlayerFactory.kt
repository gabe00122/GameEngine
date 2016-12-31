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
    private val runningAnimation = Animation(0.2f, true, true)
    private val stillAnimation = Animation(-1f, false, false)
    private val jumpingAnimation = Animation(0.15f, false, false)

    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val animatorMapper = world.getMapper(CharacterAnimatorCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val playerInputMapper = world.getMapper(PlayerInputCom::class.java)
    private val characterLegsMapper = world.getMapper(CharacterPeripheryCom::class.java)

    init{
        runningAnimation.frames.add(assets.findTexture("actors", "fred_running", 0))
        runningAnimation.frames.add(assets.findTexture("actors", "fred_running", 1))
        runningAnimation.frames.add(assets.findTexture("actors", "fred_running", 2))

        stillAnimation.frames.add(assets.findTexture("actors", "fred_running", 1))

        jumpingAnimation.frames.add(assets.findTexture("actors", "fred_jumping", 0))
        jumpingAnimation.frames.add(assets.findTexture("actors", "fred_jumping", 1))
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
        animator.jumpingAnimation = jumpingAnimation

        sprite.width = width
        sprite.height = height
        sprite.offsetY = -width/4

        //rBody
        bodyComp.rBody.addFixture(RFixture(RPolygon(width, bodyHeight), density = 1f))

        bodyComp.rBody.bodyType = BodyDef.BodyType.DynamicBody
        //bodyComp.rBody.linearDamping = 0.2f
        bodyComp.rBody.isFixedRotation = true
        bodyComp.rBody.setPosition(x, y)

        //input
        playerInput.playerInput = input

        legs.body.addFixture(RFixture(RCircle(width/2f), density = 0.5f, restitution = 0f, friction = 1f))

        legs.body.bodyType = BodyDef.BodyType.DynamicBody
        legs.body.setPosition(x, y - bodyHeight/2)

        legs.motor.bodyA = bodyComp.rBody
        legs.motor.bodyB = legs.body
        legs.motor.isMoterEnabled = true
        legs.motor.maxTorque = 5f
        legs.motor.anchorAY = -bodyHeight/2

        return id
    }

    override fun dispose() {

    }
}
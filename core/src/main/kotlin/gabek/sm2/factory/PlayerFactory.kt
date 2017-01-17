package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.*
import gabek.sm2.graphics.Animation
import gabek.sm2.input.PlayerInput
import gabek.sm2.physics.RCircle
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */
class PlayerFactory(kodein: Kodein, private val world: World) : EntityFactory {
    private val width = .5f
    private val height = 1f
    private val bodyHeight = height - width / 2f

    val arch = ArchetypeBuilder().add(
            TranslationCom::class.java,
            SpriteCom::class.java,
            AnimationCom::class.java,
            BodyCom::class.java,
            PlayerInputCom::class.java,
            CharacterStateCom::class.java,
            CharacterControllerCom::class.java,
            CharacterAnimatorCom::class.java,
            HealthCom::class.java,
            HealthDisplayCom::class.java).build(world)

    private val assets: Assets = kodein.instance()
    private val runningAnimation = Animation(0.2f, true, true)
    private val stillAnimation = Animation(-1f, false, false)
    private val jumpingAnimation = Animation(0.15f, false, false)

    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val animatorMapper = world.getMapper(CharacterAnimatorCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val stateMapper = world.getMapper(CharacterStateCom::class.java)
    private val playerInputMapper = world.getMapper(PlayerInputCom::class.java)

    private val healthMapper = world.getMapper(HealthCom::class.java)
    private val healthDisplayMapper = world.getMapper(HealthDisplayCom::class.java)

    init {
        runningAnimation.frames.add(assets.findTexture("actors", "fred_running", 0))
        runningAnimation.frames.add(assets.findTexture("actors", "fred_running", 1))
        runningAnimation.frames.add(assets.findTexture("actors", "fred_running", 2))

        stillAnimation.frames.add(assets.findTexture("actors", "fred_running", 1))

        jumpingAnimation.frames.add(assets.findTexture("actors", "fred_jumping", 0))
        jumpingAnimation.frames.add(assets.findTexture("actors", "fred_jumping", 1))
    }

    fun create(x: Float, y: Float, input: PlayerInput): Int {
        val id = world.create(arch)

        val trans = transMapper[id]
        val animator = animatorMapper[id]
        val sprite = spriteMapper[id]
        val bodyComp = bodyMapper[id]
        val state = stateMapper[id]
        val playerInput = playerInputMapper[id]
        val health = healthMapper[id]
        val healthDisplay = healthDisplayMapper[id]

        //set position
        trans.initPos(x, y)

        //sprite
        animator.runningAnimation = runningAnimation
        animator.stillAnimation = stillAnimation
        animator.jumpingAnimation = jumpingAnimation

        sprite.width = width
        sprite.height = height
        sprite.offsetY = -width / 4

        //rBody
        with(bodyComp.rBody) {
            addFixture(RFixture(RPolygon(width, bodyHeight), density = 1f))
            bodyType = BodyDef.BodyType.DynamicBody
            isFixedRotation = true
            setPosition(x, y)
        }

        //input
        playerInput.playerInput = input

        with(state.legsBody){
            addFixture(RFixture(RCircle(width / 2f), density = 0.5f, restitution = 0f, friction = 1f))
            bodyType = BodyDef.BodyType.DynamicBody
            setPosition(x, y - bodyHeight / 2)
        }

        with(state.legsMotor) {
            bodyA = bodyComp.rBody
            bodyB = state.legsBody
            isMoterEnabled = true
            maxTorque = 5f
            anchorAY = -bodyHeight / 2
        }

        health.maximumHealth = 10
        health.healthPoints = 10
        healthDisplay.offsetY = height / 2f

        return id
    }

    override fun dispose() {

    }
}
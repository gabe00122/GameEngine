package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.*
import gabek.sm2.components.character.CharacterAnimatorCom
import gabek.sm2.components.character.CharacterControllerCom
import gabek.sm2.components.character.CharacterMovementCom
import gabek.sm2.components.character.CharacterStateCom
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.graphics.HealthDisplayCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.graphics.Animation
import gabek.sm2.input.PlayerInput
import gabek.sm2.physics.RCircle
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.CHARACTER
import gabek.sm2.world.filter

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
            CharacterMovementCom::class.java,
            CharacterControllerCom::class.java,
            CharacterAnimatorCom::class.java,
            AbilityIndexCom::class.java,
            HealthCom::class.java,
            HealthDisplayCom::class.java).build(world)

    val childBodyArch = ArchetypeBuilder().add(BodyCom::class.java, ParentOfCom::class.java).build(world)

    private val assets: Assets = kodein.instance()
    private val runningAnimation = Animation(0.2f, true, true)
    private val stillAnimation = Animation(-1f, false, false)
    private val jumpingAnimation = Animation(0.15f, false, false)

    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val animatorMapper = world.getMapper(CharacterAnimatorCom::class.java)
    private val spriteMapper = world.getMapper(SpriteCom::class.java)
    private val bodyMapper = world.getMapper(BodyCom::class.java)
    private val stateMapper = world.getMapper(CharacterStateCom::class.java)
    private val movementMapper = world.getMapper(CharacterMovementCom::class.java)
    private val playerInputMapper = world.getMapper(PlayerInputCom::class.java)

    private val healthMapper = world.getMapper(HealthCom::class.java)
    private val healthDisplayMapper = world.getMapper(HealthDisplayCom::class.java)

    private val parentOfMapper = world.getMapper(ParentOfCom::class.java)

    init {
        runningAnimation.addFrames(assets, "actors", "fred_running")
        stillAnimation.addFrame(assets, "actors", "fred_running", 1)
        jumpingAnimation.addFrames(assets, "actors", "fred_jumping")
    }

    fun create(x: Float, y: Float, input: PlayerInput): Int {
        val id = world.create(arch)
        val legId = world.create(childBodyArch)

        val trans = transMapper[id]
        val animator = animatorMapper[id]
        val sprite = spriteMapper[id]
        val bodyComp = bodyMapper[id]
        val state = stateMapper[id]
        val movement = movementMapper[id]
        val playerInput = playerInputMapper[id]
        val health = healthMapper[id]
        val healthDisplay = healthDisplayMapper[id]

        val legBody = bodyMapper[legId]
        val legParent = parentOfMapper[legId]

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
            addFixture(RPolygon(width, bodyHeight), density = 1f)
            bodyType = BodyDef.BodyType.DynamicBody
            isFixedRotation = true
            setPosition(x, y)
        }

        //input
        playerInput.playerInput = input

        state.legChild = legId
        legParent.parent = id
        legParent.diesWithParent = true

        with(legBody.rBody){
            addFixture(RCircle(width / 2f), density = 0.5f, restitution = 0f, friction = 1f)
            bodyType = BodyDef.BodyType.DynamicBody
            setPosition(x, y - bodyHeight / 2)
        }

        with(state.legsMotor) {
            bodyA = bodyComp.rBody
            bodyB = legBody.rBody
            isMoterEnabled = true
            maxTorque = 5f
            anchorAY = -bodyHeight / 2
        }

        health.maximumHealth = 10f
        health.healthPoints = 10f
        healthDisplay.offsetY = height / 2f

        with(movement){
            airSpeed = 200f
            groundSpeed = 600f
            airDamping = 0.1f

            jumpCooldown = 0.1f
            jumpForce = 3.0f
        }

        return id
    }

    override fun dispose() {

    }
}
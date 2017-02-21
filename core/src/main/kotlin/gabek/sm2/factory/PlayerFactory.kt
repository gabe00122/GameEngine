package gabek.sm2.factory

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.*
import gabek.sm2.components.character.*
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.graphics.HealthDisplayCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.graphics.AnimationDef
import gabek.sm2.physics.RCircle
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.CHARACTER
import gabek.sm2.world.filter
import gabek.sm2.world.getMapper

/**
 * @author Gabriel Keith
 */


val playerFactory = factory { kodein, world ->
    val assets: Assets = kodein.instance()

    val bodyMapper = world.getMapper<BodyCom>()
    val parentMapper = world.getMapper<ParentOfCom>()

    val width = 0.5f
    val height = 1f
    val bodyHeight = height - width / 2f

    val runningAnim = AnimationDef.builder(assets)
            .setDelay(0.2f)
            .setStrategy(AnimationDef.Strategy.PINGPONG)
            .addFrames("actors:fred_running", 0..2)
            .build()

    val stillAnim = AnimationDef.builder(assets)
            .addFrame("actors:fred_running:1")
            .build()

    val jumpingAnim = AnimationDef.builder(assets)
            .setDelay(0.15f)
            .addFrames("actors:fred_jumping", 0..1)
            .build()

    val legFactory = factory { kodein, world ->
        com<ParentOfCom> { diesWithParent = true }
        com<TranslationCom>()
        com<BodyCom>{
            body.addFixture(RCircle(width / 2f), density = 0.5f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.setPosition(0f, -bodyHeight / 2)
        }
    }.build(kodein, world)

    com<TranslationCom>()
    com<BodyCom> {
        body.addFixture(RPolygon(width, bodyHeight), density = 1f, categoryBits = filter(CHARACTER))
        body.bodyType = BodyDef.BodyType.DynamicBody
        body.isFixedRotation = true
    }

    com<SpriteCom> {
        setSize(width, height)
        offsetY = -width / 4f
    }

    com<AnimationCom>()
    com<PlayerInputCom>()
    com<CharacterControllerCom>()
    com<BiDirectionCom>()
    com<CharacterMovementStateCom>()
    com<MovementPhysicsCom>{ entity ->
        wheelRef = legFactory.create()
        parentMapper[wheelRef].parent = entity

        motor.isMoterEnabled = true
        motor.maxTorque = 5f
        motor.anchorAY = -bodyHeight / 2

        motor.bodyA = bodyMapper[entity].body
        motor.bodyB = bodyMapper[wheelRef].body
    }

    com<MovementDefinitionCom> {
        airSpeed = 200f
        groundSpeed = 600f
        airDamping = 0.1f

        jumpCooldown = 0.1f
        jumpForce = 3.5f

        this.width = width
        this.height = height
        pad = 0.02f
    }

    com<CharacterAnimatorCom> {
        runningAnimationDef = runningAnim
        jumpingAnimationDef = jumpingAnim
        stillAnimationDef = stillAnim
    }

    //com<AbilityIndexCom>()
    com<HealthCom> {
        healthPoints = 10f
        maximumHealth = 10f
    }
    com<HealthDisplayCom> {
        offsetY = height / 2f
    }
}
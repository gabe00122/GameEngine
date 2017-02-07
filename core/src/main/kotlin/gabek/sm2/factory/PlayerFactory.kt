package gabek.sm2.factory

import com.badlogic.gdx.physics.box2d.BodyDef
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
import gabek.sm2.graphics.AnimationDef
import gabek.sm2.physics.RCircle
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.CHARACTER
import gabek.sm2.world.filter

/**
 * @author Gabriel Keith
 */


val playerFactory = factory { kodein, world ->
    val assets: Assets = kodein.instance()

    val width = 0.5f
    val height = 1f
    val bodyHeight = height - width / 2f

    val runningAnim = AnimationDef(0.2f, true, true)
    runningAnim.addFrames(assets, "actors", "fred_running")

    val stillAnim = AnimationDef(-1f, false, false)
    stillAnim.addFrame(assets, "actors", "fred_running", 1)

    val jumpingAnim = AnimationDef(0.15f, false, false)
    jumpingAnim.addFrames(assets, "actors", "fred_jumping")


    val legFactory = factory { kodein, world ->
        com<ParentOfCom> { diesWithParent = true }; com<BodyCom>()
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
    com<CharacterStateCom> {
        legsMotor.isMoterEnabled = true
        legsMotor.maxTorque = 5f
        legsMotor.anchorAY = -bodyHeight / 2
    }

    com<CharacterMovementCom> {
        airSpeed = 200f
        groundSpeed = 600f
        airDamping = 0.1f

        jumpCooldown = 0.1f
        jumpForce = 3.0f
    }

    com<CharacterAnimatorCom> {
        runningAnimationDef = runningAnim
        jumpingAnimationDef = jumpingAnim
        stillAnimationDef = stillAnim
    }

    com<AbilityIndexCom>()
    com<HealthCom> {
        healthPoints = 10f
        maximumHealth = 10f
    }
    com<HealthDisplayCom> {
        offsetY = height / 2f
    }

    onCreate { entity ->
        with(generalMapper) {
            val legs = legFactory.create()
            val legBody = bodyMapper[legs].body
            val entityBody = bodyMapper[entity].body

            parentOfMapper[legs].parent = entity

            with(legBody) {
                addFixture(RCircle(width / 2f), density = 0.5f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))
                bodyType = BodyDef.BodyType.DynamicBody
                setPosition(x, y - bodyHeight / 2)
            }

            with(characterStateMapper[entity]) {
                legChild = legs
                legsMotor.bodyA = entityBody
                legsMotor.bodyB = legBody
            }
        }
    }
}
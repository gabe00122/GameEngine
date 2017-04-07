package gabek.sm2.factory

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.PlayerInputCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.character.*
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.graphics.HealthDisplayCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.CHARACTER
import gabek.sm2.world.WALL
import gabek.sm2.world.filter

/**
 * @author Gabriel Keith
 */


class PlayerFactory: EntityFactory(){
    override fun define() {
        val assets: Assets = kodein.instance()

        val width = 0.5f
        val height = 1f
        val bodyHeight = height - width / 2f

        val runningAnim = assets.retrieveAnimationDef("fred:running")
        val stillAnim = assets.retrieveAnimationDef("fred:still")
        val jumpingAnim = assets.retrieveAnimationDef("fred:jumping")

        //val legFactory = factory { kodein, world ->
        //    com<ParentOfCom> { diesWithParent = true }
        //    com<TranslationCom>()
        //    com<BodyCom> {
        //        body.addFixture(RCircle(width / 2f), density = 0.5f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))
        //        body.bodyType = BodyDef.BodyType.DynamicBody
        //        body.setPosition(0f, -bodyHeight / 2)
        //    }
        //}.build(kodein, world)

        com<TranslationCom>()
        com<BodyCom> {
            //val bodyShape = RPolygon()
            //bodyShape.setAsBox(width, bodyHeight, 0f, 0f)
            //body.addFixture(bodyShape, density = 1f, categoryBits = filter(CHARACTER))
            val platformShape = RPolygon().withClippedCorners(width, height, 0f, 0f, width / 4, 0.25f)
            body.addFixture(platformShape, density = 1f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER), maskBits = filter(WALL))

            body.bodyType = BodyDef.BodyType.DynamicBody
            body.isFixedRotation = true
        }

        com<SpriteCom> {
            setSize(width + 0.025f, height + 0.025f)
            //offsetY = -width / 4f
            layer = 2
        }

        com<AnimationCom>()
        com<PlayerInputCom>()
        com<CharacterControllerCom>()
        com<BiDirectionCom>()
        com<CharacterMovementStateCom>()
        com<MovementGroundContactCom> {
            platformIndex = 0
        }
        //com<MovementPhysicsWheelCom> { entity ->
        //    wheelRef = legFactory.create()
        //    parentMapper[wheelRef].parent = entity

        //    motor.isMoterEnabled = true
        //    motor.maxTorque = 5f
        //    motor.anchorAY = -bodyHeight / 2

        //    motor.bodyA = bodyMapper[entity].body
        //    motor.bodyB = bodyMapper[wheelRef].body
        //}

        com<MovementDefinitionCom> {
            airSpeed = 3f
            groundSpeed = 3f
            airDamping = 0.1f

            jumpCooldown = 0.1f
            jumpForce = 2.5f

            this.width = width
            this.height = height
            pad = 0.01f
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
            offsetY = height * 0.75f
        }
    }
}
package gabek.engine.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.components.BodyCom
import gabek.engine.components.PlayerInputCom
import gabek.engine.components.character.*
import gabek.engine.components.common.TranslationCom
import gabek.engine.components.graphics.AnimationCom
import gabek.engine.components.graphics.HealthDisplayCom
import gabek.engine.components.graphics.SpriteCom
import gabek.engine.physics.RPolygon
import gabek.engine.world.CHARACTER
import gabek.engine.world.filter

/**
 * @author Gabriel Keith
 */


class PlayerPrefab : Prefab() {
    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()

        val width = 0.5f
        val height = 1f
        val bodyHeight = height - width / 2f

        val runningAnim = assets.findAnimation("fred:running")
        val stillAnim = assets.findAnimation("fred:still")
        val jumpingAnim = assets.findAnimation("fred:jumping")

        //val legFactory = prefab { kodein, world ->
        //    com<ParentOfCom> { diesWithParent = true }
        //    com<TranslationCom>()
        //    com<BodyCom> {
        //        body.addFixture(RCircle(width / 2f), density = 0.5f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))
        //        body.bodyType = BodyDef.BodyType.DynamicBody
        //        body.setPosition(0f, -bodyHeight / 2)
        //    }
        //}.build(kodein, world)

        add<TranslationCom>()
        add<BodyCom> {
            //val bodyShape = RPolygon()
            //bodyShape.setAsBox(width, bodyHeight, 0f, 0f)
            //body.addFixture(bodyShape, density = 1f, categoryBits = filter(CHARACTER))
            val platformShape = RPolygon().withClippedCorners(width, height, 0f, 0f, width / 4, 0.25f)
            body.addFixture(platformShape, density = 1f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))

            body.bodyType = BodyDef.BodyType.DynamicBody
            body.isFixedRotation = true
        }

        add<SpriteCom> {
            setSize(width + 0.025f, height + 0.025f)
            //offsetY = -width / 4f
            layer = 2
        }

        add<AnimationCom>()
        add<PlayerInputCom>()
        add<CharacterControllerCom>()
        add<BiDirectionCom>()
        add<CharacterMovementStateCom>()
        add<MovementGroundContactCom> {
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

        add<MovementDefinitionCom> {
            airSpeed = 3f
            groundSpeed = 3f
            airDamping = 0.1f

            jumpCooldown = 0.1f
            jumpForce = 2.5f

            this.width = width
            this.height = height
            pad = 0.01f
        }

        add<CharacterAnimatorCom> {
            runningAnimationDef = runningAnim
            jumpingAnimationDef = jumpingAnim
            stillAnimationDef = stillAnim
        }

        //com<AbilityIndexCom>()
        add<HealthCom> {
            value = 10f
            maximum = 10f
        }
        add<HealthDisplayCom> {
            offsetY = height * 0.75f
        }
    }
}
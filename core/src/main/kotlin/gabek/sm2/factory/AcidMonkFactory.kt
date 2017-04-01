package gabek.sm2.factory

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
import gabek.sm2.world.filter

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */


fun acidMonkFactory() = factory { kodein, world ->
    val assets: Assets = kodein.instance()

    val width = 0.5f
    val height = 1f
    val bodyHeight = height - width / 2f

    //val runningAnim = assets.retrieveAnimationDef("fred:running")
    val stillAnim = assets.retrieveAnimationDef("acid_monk:still")
    //val jumpingAnim = assets.retrieveAnimationDef("fred:jumping")

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
        body.addFixture(platformShape, density = 1f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))

        body.bodyType = BodyDef.BodyType.DynamicBody
        body.isFixedRotation = true
    }

    com<SpriteCom> {
        setSize(width + 0.025f, height + 0.025f)
        //offsetY = -width / 4f
    }

    com<AnimationCom>()
    com<CharacterControllerCom>()
    com<BiDirectionCom>()
    com<CharacterMovementStateCom>()
    com<MovementGroundContactCom>{
        platformIndex = 0
    }

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
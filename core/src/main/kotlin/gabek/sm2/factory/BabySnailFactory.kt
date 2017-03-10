package gabek.sm2.factory

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.PlayerInputCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.brains.WanderingBrainCom
import gabek.sm2.components.character.*
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.physics.RPolygon

/**
 * @author Gabriel Keith
 */

fun babySnailFactory() = factory { kodein, world ->
    val assets: Assets = kodein.instance()

    val snailMoving = assets.retrieveAnimationDef("baby_snail:moving")
    val snailStill = assets.retrieveAnimationDef("baby_snail:still")

    val width = 1f
    val height = 0.5f

    com<TranslationCom>()
    com<SpriteCom> {
        this.width = width
        this.height = height
    }
    com<AnimationCom>()

    com<CharacterAnimatorCom> {
        runningAnimationDef = snailMoving
        stillAnimationDef = snailStill
    }
    com<CharacterMovementStateCom>()

    com<WanderingBrainCom>()

    com<CharacterControllerCom>()

    com<MovementDefinitionCom> {
        groundSpeed = 0.5f
        jumpCooldown = 0.1f
        jumpForce = 2f
    }
    com<MovementGroundContactCom>()

    com<BodyCom> {
        body.bodyType = BodyDef.BodyType.DynamicBody
        val platform = RPolygon()
        platform.withClippedCorners(width, height * (1f / 4f), 0f, -height * (3f / 8f), 0.1f)

        body.addFixture(platform, 1f, 0.2f, 0.1f)
        body.addFixture(RPolygon(width, height * (3f / 4f), 0f, height * (1f / 8f)), 0.75f, 1f, 0.1f)
        body.isSleepingAllowed = false
        //body.isFixedRotation = true
    }
    com<BiDirectionCom>()
}
package gabek.sm2.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.PlayerInputCom
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.components.brains.WanderingBrainCom
import gabek.sm2.components.character.*
import gabek.sm2.components.graphics.AnimationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.CHARACTER
import gabek.sm2.world.filter

/**
 * @author Gabriel Keith
 */

class SnailPrefab : Prefab(){

    override fun define() {
        val assets: Assets = kodein.instance()

        val snailMoving = assets.retrieveAnimationDef("baby_snail:moving")
        val snailStill = assets.retrieveAnimationDef("baby_snail:still")

        val width = 1f
        val height = 0.5f

        add<TranslationCom>()
        add<SpriteCom> {
            this.width = width + 0.025f
            this.height = height + 0.025f
        }
        add<AnimationCom>()

        add<CharacterAnimatorCom> {
            runningAnimationDef = snailMoving
            stillAnimationDef = snailStill
        }
        add<CharacterMovementStateCom>()

        add<WanderingBrainCom>()

        add<CharacterControllerCom>()

        add<MovementDefinitionCom> {
            groundSpeed = 0.5f
            jumpCooldown = 0.01f
            jumpForce = 2f
        }
        add<MovementGroundContactCom> {
            platformIndex = 0
        }

        add<BodyCom> {
            body.bodyType = BodyDef.BodyType.DynamicBody
            val platform = RPolygon().withClippedCorners(width, height, 0f, 0f, 0.1f, height / 4f)
            body.addFixture(platform, 1f, 0.75f, 0f, false, filter(CHARACTER))
            //body.isFixedRotation = true
        }
        add<BiDirectionCom>()
        add<HealthCom> {
            value = 3f
            maximum = 3f
        }
    }
}
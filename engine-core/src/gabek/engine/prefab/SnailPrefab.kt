package gabek.engine.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.components.BodyCom
import gabek.engine.components.brains.WanderingBrainCom
import gabek.engine.components.character.*
import gabek.engine.components.common.TranslationCom
import gabek.engine.components.graphics.AnimationCom
import gabek.engine.components.graphics.SpriteCom
import gabek.engine.physics.RPolygon
import gabek.engine.world.CHARACTER
import gabek.engine.world.filter

/**
 * @author Gabriel Keith
 */

class SnailPrefab : Prefab() {

    override fun define() {
        super.define()
        val assets: Assets = kodein.instance()

        val snailMoving = assets.findAnimation("baby_snail:moving")
        val snailStill = assets.findAnimation("baby_snail:still")

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
        add<BiDirectionCom>()
        add<CharacterControllerCom>()
        add<MovementDefinitionCom> {
            groundSpeed = 0.5f
            jumpCooldown = 0.01f
            jumpForce = 2f
        }
        add<MovementGroundContactCom> {
            platformIndex = 0
        }

        add<WanderingBrainCom>()

        add<BodyCom> {
            body.bodyType = BodyDef.BodyType.DynamicBody
            val platform = RPolygon().withClippedCorners(width, height, 0f, 0f, 0.1f, height / 4f)
            body.addFixture(platform, 1f, 0.75f, 0f, false, filter(CHARACTER))
            body.isFixedRotation = true
        }
        add<HealthCom> {
            value = 3f
            maximum = 3f
        }
    }
}
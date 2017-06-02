package gabek.spacemonk.prefab

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.physics.shape.RPolygon
import gabek.spacemonk.CHARACTER
import gabek.spacemonk.component.CharacterAnimatorCom
import gabek.spacemonk.component.WanderingBrainCom

/**
 * @author Gabriel Keith
 */

class SnailPrefab : gabek.engine.core.prefab.Prefab() {

    override fun define() {
        super.define()
        val assets: gabek.engine.core.assets.Assets = kodein.instance()

        val snailMoving = assets.getAnimation("baby_snail:moving")
        val snailStill = assets.getAnimation("baby_snail:still")

        val width = 1f
        val height = 0.5f

        add<gabek.engine.core.components.common.TranslationCom>()
        add<gabek.engine.core.components.graphics.SpriteCom> {
            this.width = width + 0.025f
            this.height = height + 0.025f
        }
        add<gabek.engine.core.components.graphics.AnimationCom>()

        add<CharacterAnimatorCom> {
            runningAnimationRef = snailMoving
            stillAnimationRef = snailStill
        }
        add<gabek.engine.core.components.character.CharacterMovementStateCom>()
        add<gabek.engine.core.components.character.BiDirectionCom>()
        add<gabek.engine.core.components.character.CharacterControllerCom>()
        add<gabek.engine.core.components.character.MovementDefinitionCom> {
            groundSpeed = 0.5f
            jumpCooldown = 0.01f
            jumpForce = 2f
        }
        add<gabek.engine.core.components.character.MovementGroundContactCom> {
            platformIndex = 0
        }

        add<WanderingBrainCom>()

        add<gabek.engine.core.components.BodyCom> {
            body.bodyType = com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
            val platform = RPolygon().withClippedCorners(width, height, 0f, 0f, 0.1f, height / 4f)
            body.addFixture(platform, 1f, 0.75f, 0f, false, gabek.engine.core.world.filter(CHARACTER))
            body.isFixedRotation = true
        }
        add<gabek.engine.core.components.character.HealthCom> {
            value = 3f
            maximum = 3f
        }
    }
}
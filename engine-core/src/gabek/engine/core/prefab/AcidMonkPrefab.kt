package gabek.engine.core.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.character.*
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.AnimationCom
import gabek.engine.core.components.graphics.HealthDisplayCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.physics.RPolygon
import gabek.engine.core.world.CHARACTER
import gabek.engine.core.world.filter

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */


class AcidMonkPrefab : Prefab() {

    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()

        val width = 0.5f
        val height = 1f
        val bodyHeight = height - width / 2f


        val stillAnim = assets.findAnimation("acid_monk:still")


        add<TranslationCom>()
        add<BodyCom> {
            val platformShape = RPolygon().withClippedCorners(width, height, 0f, 0f, width / 4, 0.25f)
            body.addFixture(platformShape, density = 1f, restitution = 0f, friction = 1f, categoryBits = filter(CHARACTER))

            body.bodyType = BodyDef.BodyType.DynamicBody
            body.isFixedRotation = true
        }

        add<SpriteCom> {
            setSize(width + 0.025f, height + 0.025f)
            //offsetY = -width / 4f
        }

        add<AnimationCom>()
        add<CharacterControllerCom>()
        add<BiDirectionCom>()
        add<CharacterMovementStateCom>()
        add<MovementGroundContactCom> {
            platformIndex = 0
        }

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
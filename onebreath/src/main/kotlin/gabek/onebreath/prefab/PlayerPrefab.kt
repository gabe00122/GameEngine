package gabek.onebreath.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.InputCom
import gabek.engine.core.components.channel.DirectionalInputCom
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.physics.shape.RPolygon
import gabek.engine.core.prefab.Prefab
import gabek.engine.quick.water.BuoyantCom
import gabek.onebreath.component.BiDirectionCom
import gabek.onebreath.component.CharacterMovementStateCom
import gabek.onebreath.component.MovementDefinitionCom
import gabek.onebreath.component.MovementGroundContactCom

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */

class PlayerPrefab: Prefab() {
    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()
        val pixelRatio: PixelRatio = kodein.instance()

        val sprite = assets.getTexture("actors:player_ideal")
        val w = 14 * pixelRatio.pixelToMeters
        val h = 28 * pixelRatio.pixelToMeters

        add<TranslationCom>()


        add<SpriteCom> {
            this.sprite = sprite
        }
        add<SizeCom> {
            width = w
            height = h
        }

        add<BodyCom> {
            val shape = RPolygon().withClippedCorners(w, h,
                    0f, 0f,
                    0.75f,
                    0.25f)

            body.addFixture(shape, 1.1f, 1f, 0f)
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.isFixedRotation = true
        }

        add<BiDirectionCom>{ invert = true }
        add<CharacterMovementStateCom>()
        add<MovementGroundContactCom>()

        add<MovementDefinitionCom> {
            airSpeed = 3f
            groundSpeed = 3f
            airDamping = 0.1f

            jumpCooldown = 0.1f
            jumpForce = 4f
        }

        add<InputCom>()
        add<DirectionalInputCom>{
            //panX = -1f
        }

        add<BuoyantCom>()
    }
}
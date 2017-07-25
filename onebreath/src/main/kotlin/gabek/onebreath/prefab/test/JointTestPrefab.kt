package gabek.onebreath.prefab.test

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.physics.shape.RPolygon
import gabek.engine.core.prefab.Prefab

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */
open class JointTestPrefab: Prefab(){
    override fun define() {
        super.define()
        val assets: Assets = kodein.instance()

        val pixelToMeter = kodein.instance<PixelRatio>().pixelToMeters
        val sprite = assets.getTexture("actors:joint_test")

        val w = 32 * pixelToMeter
        val h = 16 * pixelToMeter

        add<TranslationCom>()
        add<BodyCom> {
            body.addFixture(RPolygon(w, h), 2.5f, 1f, 0.2f)
            body.bodyType = BodyDef.BodyType.DynamicBody
        }

        add<SpriteCom> {
            this.sprite = sprite
        }

        add<SizeCom> {
            width = w
            height = h
        }
    }
}
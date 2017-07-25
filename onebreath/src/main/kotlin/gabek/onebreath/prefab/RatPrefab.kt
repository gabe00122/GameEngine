package gabek.onebreath.prefab

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
import gabek.engine.quick.water.BuoyantCom

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */
class RatPrefab: Prefab(){

    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()
        val pixelRatio: PixelRatio = kodein.instance()

        val sprite = assets.getTexture("actors:rat_ideal")

        val w = 15 * pixelRatio.pixelToMeters
        val h = 8 * pixelRatio.pixelToMeters

        add<TranslationCom>()
        add<SpriteCom> {
            this.sprite = sprite
        }
        add<SizeCom> {
            width = w
            height = h
        }

        add<BodyCom> {
            val shape = RPolygon(w, h)

            body.addFixture(shape, 1.2f, 1f, 0f)
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.isFixedRotation = true
        }

        add<BuoyantCom>()
    }

}
package gabek.onebreath.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.physics.shape.RPolygon
import gabek.engine.core.prefab.Prefab
import gabek.engine.quickbuoyancy.BuoyantCom

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */
class RatPrefab: Prefab(){

    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()
        val sprite = assets.findTexture("actors:rat:0")

        val radiusScale = 1f - 0.05f
        val w = 0.5f
        val h = 0.25f

        add<TranslationCom>()
        add<SpriteCom> {
            textureRef = sprite
            width = 0.5f
            height = 0.25f
        }

        add<BodyCom> {
            val shape = RPolygon(
                    w * radiusScale,
                    h * radiusScale)

            body.addFixture(shape, 1.2f, 1f, 0f)
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.isFixedRotation = true
        }

        add<BuoyantCom>()
    }

}
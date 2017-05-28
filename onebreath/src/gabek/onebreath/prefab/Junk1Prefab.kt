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

class Junk1Prefab: Prefab(){
    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()
        val texture = assets.findTexture("actors:box")

        val scale = 0.95f
        val w = 0.25f
        val h = 0.25f

        add<TranslationCom>()
        add<SpriteCom>{
            textureRef = texture
            width = w
            height = h
        }

        add<BodyCom>{
            body.addFixture(RPolygon(w * scale, h * scale), 0.75f, 0.4f, 0.2f)
            body.bodyType = BodyDef.BodyType.DynamicBody
        }
        add<BuoyantCom>()
    }
}
package gabek.sm2.prefab

import com.badlogic.gdx.graphics.Color
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.LifeSpanCom
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.components.pellet.PelletMovementCom
import java.util.*

/**
 * @author Gabriel Keith
 * @date 3/29/2017
 */

class BloodPrefab : Prefab() {
    override fun define() {
        super.define()

        val rect = kodein.instance<Assets>().findTexture("actors:rect")
        val color = Color(172f / 255, 50f / 255, 50f / 255, 0.75f)
        val w = 0.2f
        val h = 0.2f

        val random = Random()

        add<TranslationCom>()
        add<SpriteCom> {
            textureRef = rect
            tint = color
            width = w
            height = h
        }
        //com<BodyCom> {
        //    body.bodyType = BodyDef.BodyType.DynamicBody
        //    body.isFixedRotation = true
        //    body.addFixture(RPolygon(w, h), 1f, 0.2f, 0.75f, maskBits = filter(WALL))
        //}
        add<PelletMovementCom> {
            gravity = 9f

            speedX = (random.nextFloat() - 0.5f) * 4f
            speedY = random.nextFloat() * 8
        }
        add<LifeSpanCom> {
            lifeSpan = 5f + random.nextFloat()
        }
    }
}
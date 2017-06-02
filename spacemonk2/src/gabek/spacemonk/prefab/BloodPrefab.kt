package gabek.spacemonk.prefab

import com.github.salomonbrys.kodein.instance

/**
 * @author Gabriel Keith
 * @date 3/29/2017
 */

class BloodPrefab : gabek.engine.core.prefab.Prefab() {
    override fun define() {
        super.define()

        val rect = kodein.instance<gabek.engine.core.assets.Assets>().getTexture("actors:rect")
        val color = com.badlogic.gdx.graphics.Color(172f / 255, 50f / 255, 50f / 255, 0.75f)
        val w = 0.2f
        val h = 0.2f

        val random = java.util.Random()

        add<gabek.engine.core.components.common.TranslationCom>()
        add<gabek.engine.core.components.graphics.SpriteCom> {
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
        add<gabek.engine.core.components.pellet.PelletMovementCom> {
            gravity = 9f

            speedX = (random.nextFloat() - 0.5f) * 4f
            speedY = random.nextFloat() * 8
        }
        add<gabek.engine.core.components.LifeSpanCom> {
            lifeSpan = 5f + random.nextFloat()
        }
    }
}
package gabek.sm2.factory

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.components.pellet.PelletLifeSpanCom
import gabek.sm2.components.pellet.PelletMovementCom
import gabek.sm2.graphics.TextureRef
import gabek.sm2.physics.RPolygon
import gabek.sm2.settings.Settings
import gabek.sm2.world.WALL
import gabek.sm2.world.filter
import java.util.*

/**
 * @author Gabriel Keith
 * @date 3/29/2017
 */

fun bloodDroplets() = factory { kodein, world ->
    val rect = kodein.instance<Assets>().findTexture("actors:rect")
    val color = Color(172f/255, 50f/255, 50f/255, 0.75f)
    val w = 0.2f
    val h = 0.2f

    val random = Random()

    com<TranslationCom>()
    com<SpriteCom>{
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
    com<PelletMovementCom>{
        gravity = 9f

        speedX = (random.nextFloat() - 0.5f) * 4f
        speedY = random.nextFloat() * 8
    }
    com<PelletLifeSpanCom>{
        lifeSpan = 5f + random.nextFloat()
    }
}
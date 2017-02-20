package gabek.sm2.factory

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.*

/**
 * @author Gabriel Keith
 */

val junkFactory = factory { kodein, world ->
    val texture = kodein.instance<Assets>().findTexture("actors:junk")

    val width = 1f
    val height = 1f

    com<TranslationCom>()
    com<SpriteCom>{
        this.textureRef = texture
        this.width = width
        this.height = height
    }

    com<BodyCom>{
        body.bodyType = BodyDef.BodyType.DynamicBody
        body.addFixture(RPolygon(width, height),
                friction = 0.8f,
                density = 0.5f,
                categoryBits = category(PROP_LARGE))
                //maskBits = filter(PROP_LARGE, PROP_SMALL, WALL))

    }

}
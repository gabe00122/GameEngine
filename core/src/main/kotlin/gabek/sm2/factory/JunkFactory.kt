package gabek.sm2.factory

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.physics.RPolygon
import gabek.sm2.world.PROP_LARGE
import gabek.sm2.world.category

/**
 * @author Gabriel Keith
 */

class JunkFactory: EntityFactory(){
    override fun define() {
        val texture = kodein.instance<Assets>().findTexture("actors:junk")

        val width = 0.75f
        val height = 0.75f

        com<TranslationCom>()
        com<SpriteCom> {
            this.textureRef = texture
            setSize(width + 0.025f, height + 0.025f)
        }

        com<BodyCom> {
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.addFixture(RPolygon(width, height),
                    friction = 0.8f,
                    density = 0.5f,
                    categoryBits = category(PROP_LARGE))
            //maskBits = filter(PROP_LARGE, PROP_SMALL, WALL))

        }
    }
}
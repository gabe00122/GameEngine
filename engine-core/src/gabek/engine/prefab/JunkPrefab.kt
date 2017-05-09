package gabek.engine.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.components.BodyCom
import gabek.engine.components.common.TranslationCom
import gabek.engine.components.graphics.SpriteCom
import gabek.engine.physics.RPolygon
import gabek.engine.world.PROP_LARGE
import gabek.engine.world.category

/**
 * @author Gabriel Keith
 */

class JunkPrefab : Prefab() {
    override fun define() {
        super.define()

        val texture = kodein.instance<Assets>().findTexture("actors:junk")

        val width = 0.75f
        val height = 0.75f

        add<TranslationCom>()
        add<SpriteCom> {
            this.textureRef = texture
            setSize(width + 0.025f, height + 0.025f)
        }

        add<BodyCom> {
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.addFixture(RPolygon(width, height),
                    friction = 0.8f,
                    density = 0.5f,
                    categoryBits = category(PROP_LARGE))
            //maskBits = filter(PROP_LARGE, PROP_SMALL, WALL))

        }
    }
}
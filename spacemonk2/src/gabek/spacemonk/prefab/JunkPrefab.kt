package gabek.spacemonk.prefab

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.physics.shape.RPolygon

/**
 * @author Gabriel Keith
 */

class JunkPrefab : gabek.engine.core.prefab.Prefab() {
    override fun define() {
        super.define()

        val texture = kodein.instance<gabek.engine.core.assets.Assets>().findTexture("actors:junk")

        val width = 0.75f
        val height = 0.75f

        add<gabek.engine.core.components.common.TranslationCom>()
        add<gabek.engine.core.components.graphics.SpriteCom> {
            this.textureRef = texture
            setSize(width + 0.025f, height + 0.025f)
        }

        add<gabek.engine.core.components.BodyCom> {
            body.bodyType = com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
            body.addFixture(RPolygon(width, height),
                    friction = 0.8f,
                    density = 0.5f,
                    categoryBits = gabek.engine.core.world.category(gabek.engine.core.world.PROP_LARGE))
        }
    }
}
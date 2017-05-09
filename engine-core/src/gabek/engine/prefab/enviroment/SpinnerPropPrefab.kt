package gabek.engine.prefab.enviroment

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.components.BodyCom
import gabek.engine.components.common.TranslationCom
import gabek.engine.components.graphics.SpriteCom
import gabek.engine.physics.RFixture
import gabek.engine.physics.RPolygon
import gabek.engine.prefab.Prefab
import gabek.engine.util.getMapper

/**
 * @author Gabriel Keith
 * @date 3/31/2017
 */

class SpinnerPropPrefab : Prefab() {
    override fun define() {
        super.define()

        val bodyMapper = world.getMapper<BodyCom>()

        val sprite = kodein.instance<Assets>().findTexture("actors:junk")
        val w = 1f
        val h = 1f

        add<TranslationCom>()
        add<BodyCom> {
            body.bodyType = BodyDef.BodyType.DynamicBody
            body.addFixture(RFixture(RPolygon(w, h), 0.5f, 0.75f, 0.1f))
        }

        add<SpriteCom> {
            textureRef = sprite
            width = w
            height = h
        }

        //add<StaticJointCom> { entity ->
        //    val body = bodyMapper[entity].body

        //    val moter = RRevoluteJoint()
        //    joint = moter

        //    moter.bodyA = body
        //    moter.motorSpeed = 90f
        //    moter.maxTorque = 10f
        //    moter.isMoterEnabled = true
        //}
    }
}
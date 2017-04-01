package gabek.sm2.factory.enviroment

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.components.BodyCom
import gabek.sm2.components.StaticJointCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.components.graphics.SpriteCom
import gabek.sm2.factory.factory
import gabek.sm2.physics.RFixture
import gabek.sm2.physics.RPolygon
import gabek.sm2.physics.joints.RRevoluteJoint
import gabek.sm2.world.getMapper

/**
 * @author Gabriel Keith
 * @date 3/31/2017
 */

fun spinnerFactory() = factory { kodein, world ->
    val bodyMapper = world.getMapper<BodyCom>()

    val sprite = kodein.instance<Assets>().findTexture("actors:junk")
    val w = 1f
    val h = 1f

    com<TranslationCom>()
    com<BodyCom>{
        body.bodyType = BodyDef.BodyType.DynamicBody
        body.addFixture(RFixture(RPolygon(w, h), 0.5f, 0.75f, 0.1f))
    }

    com<SpriteCom>{
        textureRef = sprite
        width = w
        height = h
    }

    com<StaticJointCom> { entity ->
        val body = bodyMapper[entity].body

        val moter = RRevoluteJoint()
        joint = moter

        moter.bodyA = body
        moter.motorSpeed = 90f
        moter.maxTorque = 10f
        moter.isMoterEnabled = true
    }
}
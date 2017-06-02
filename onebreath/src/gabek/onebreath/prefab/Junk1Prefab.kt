package gabek.onebreath.prefab

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.JointCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.physics.joint.RRevoluteJoint
import gabek.engine.core.physics.shape.RPolygon
import gabek.engine.core.prefab.Prefab
import gabek.engine.quick.water.BuoyantCom

/**
 * @another Gabriel Keith
 * @date 5/28/2017.
 */

class Junk1Prefab: Prefab(){
    override fun define() {
        super.define()

        val assets: Assets = kodein.instance()
        val texture = assets.getTexture("actors:box")

        val scale = 0.90f
        val w = 0.25f
        val h = 0.25f

        add<TranslationCom>()
        add<SpriteCom>{
            textureRef = texture
            width = w
            height = h
        }

        add<BodyCom> {
            body.addFixture(RPolygon(w * scale, h * scale), 0.75f, 0.4f, 0.2f)
            body.bodyType = BodyDef.BodyType.DynamicBody
            //body.gravityScale = 0f
        }

        //add<JointCom> {
        //    sourceType = JointCom.SourceType.SELF
        //    destinationType = JointCom.DestinationType.STATIC
//
        //    val revJoint = RRevoluteJoint()
        //    revJoint.isMotorEnabled = true
        //    revJoint.maxTorque = 1f
        //    revJoint.motorSpeed = 180f
//
        //    joint = revJoint
        //}

        add<BuoyantCom>()
    }
}
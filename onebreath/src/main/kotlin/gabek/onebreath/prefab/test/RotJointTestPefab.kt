package gabek.onebreath.prefab.test

import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.assets.Assets
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.JointCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.SpriteCom
import gabek.engine.core.physics.joint.RRevoluteJoint
import gabek.engine.core.physics.shape.RPolygon
import gabek.engine.core.prefab.Prefab
import gabek.onebreath.component.MotorOscillatorCom

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */
class RotJointTestPefab: JointTestPrefab(){
    override fun define() {
        super.define()

        add<JointCom> {
            val rev = RRevoluteJoint()
            rev.isMotorEnabled = true
            rev.maxTorque = 1f
            rev.motorSpeed = 180f
            joint = rev
        }

        add<MotorOscillatorCom>()
    }
}
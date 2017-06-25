package gabek.onebreath.prefab.test

import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.JointCom
import gabek.engine.core.physics.joint.RPrismaticJoint
import gabek.engine.core.physics.joint.RRevoluteJoint
import gabek.onebreath.component.MotorOscillatorCom

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */
class PrisJointTestPrefab: JointTestPrefab(){
    override fun define() {
        super.define()

        add<JointCom> {
            val j = RPrismaticJoint()
            j.isMotorEnabled = true
            j.isLimitEnabled = true
            j.maxMotorForce = 10f
            j.motorSpeed = 2.5f
            j.lowerTranslation = 0f
            j.upperTranslation = 5f
            j.localAxisA.set(1f, 0f)
            joint = j
        }

        add<MotorOscillatorCom>()
    }
}
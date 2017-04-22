package gabek.sm2.components

import gabek.sm2.physics.joints.RJoint

/**
 * @author Gabriel Keith
 * @date 3/31/2017
 */
class StaticJointCom: RComponent<StaticJointCom>(){
    var joint: RJoint? = null


    override fun reset() {
        joint = null
    }

    override fun set(other: StaticJointCom) {
        TODO("joint copy function")
    }

    override fun toString() = "StaticJointCom"
}
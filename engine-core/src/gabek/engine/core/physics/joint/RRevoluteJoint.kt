package gabek.engine.core.physics.joint

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Joint
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import gabek.engine.core.physics.RWorld
import gabek.engine.core.physics.TEMP_REVOLUTE_DEF

/**
 * @author Gabriel Keith
 */
class RRevoluteJoint: RJoint(), Motorized {
    private var revoluteJoint: RevoluteJoint? = null

    override var isMotorEnabled: Boolean = false
        set(value) {
            field = value
            revoluteJoint?.enableMotor(value)
        }

    var isLimitEnabled: Boolean = false
        set(value) {
            field = value
            revoluteJoint?.enableLimit(value)
        }

    var referenceAngleRad: Float = 0f
        set(value) {
            field = value
            if (joint != null) {
                throw IllegalStateException("Can't change referenceAngleRad after joint creation.")
            }
        }

    var referenceAngle: Float
        get() = referenceAngleRad * MathUtils.radiansToDegrees
        set(value) {
            referenceAngleRad = value * MathUtils.degreesToRadians
        }

    var lowerLimitRad: Float = 0f
        set(value) {
            field = value
            revoluteJoint?.setLimits(field, upperLimitRad)
        }

    var lowerLimit: Float
        get() = lowerLimitRad * MathUtils.radiansToDegrees
        set(value) {
            lowerLimitRad = value * MathUtils.degreesToRadians
        }

    var upperLimitRad: Float = 0f
        set(value) {
            field = value
            revoluteJoint?.setLimits(lowerLimitRad, value)
        }

    var upperLimit: Float
        get() = upperLimitRad * MathUtils.radiansToDegrees
        set(value) {
            upperLimitRad = value * MathUtils.degreesToRadians
        }

    var maxTorque: Float = 0f
        set(value) {
            field = value
            revoluteJoint?.maxMotorTorque = value
        }

    var motorSpeedRad: Float = 0f
        set(value) {
            field = value
            revoluteJoint?.motorSpeed = value
        }

    override var motorSpeed: Float
        get() = motorSpeedRad * MathUtils.radiansToDegrees
        set(value) {
            motorSpeedRad = value * MathUtils.degreesToRadians
        }

    val jointAngle: Float
        get(){
            val joint = revoluteJoint
            if(joint != null){
                return joint.jointAngle * MathUtils.radiansToDegrees
            } else {
                return 0f
            }
        }

    override fun update() {}

    override fun initialise() {
        joint = world!!.createJoint(def)
        revoluteJoint = joint as RevoluteJoint
    }

    override fun store() {
        val joint = revoluteJoint
        revoluteJoint = null
        this.joint = null

        if(joint != null) {
            world?.destroyJoint(joint)
        }
        world = null
    }

    private val def: RevoluteJointDef
        get() {
            val def = TEMP_REVOLUTE_DEF
            def.bodyA = bodyA!!.body!!
            def.bodyB = bodyB!!.body!!
            def.localAnchorA.set(anchorA)
            def.localAnchorB.set(anchorB)
            def.collideConnected = collideConnected

            def.enableMotor = isMotorEnabled
            def.enableLimit = isLimitEnabled
            def.referenceAngle = referenceAngleRad
            def.lowerAngle = lowerLimitRad
            def.upperAngle = upperLimitRad
            def.maxMotorTorque = maxTorque
            def.motorSpeed = motorSpeedRad

            return def
        }

    override fun clone(): RJoint {
        val clonedJoint = RRevoluteJoint()

        //clonedJoint.setAnchorA(anchorA)
        //clonedJoint.setAnchorB(anchorB)
        clonedJoint.collideConnected = collideConnected


        clonedJoint.isMotorEnabled = isMotorEnabled
        clonedJoint.isLimitEnabled = isLimitEnabled
        clonedJoint.referenceAngleRad = referenceAngleRad
        clonedJoint.lowerLimitRad = lowerLimitRad
        clonedJoint.upperLimitRad = upperLimitRad
        clonedJoint.maxTorque = maxTorque
        clonedJoint.motorSpeedRad = motorSpeedRad

        return clonedJoint
    }
}
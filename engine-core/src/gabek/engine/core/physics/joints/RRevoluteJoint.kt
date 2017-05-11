package gabek.engine.core.physics.joints

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef

/**
 * @author Gabriel Keith
 */
class RRevoluteJoint: RJoint() {
    private var revoluteJoint: RevoluteJoint? = null

    var isMoterEnabled: Boolean = false
        get() = revoluteJoint?.isMotorEnabled ?: field
        set(value) {
            val joint = revoluteJoint
            if (joint != null) {
                joint.enableMotor(value)
            } else {
                field = value
            }
        }

    var isLimitEnabled: Boolean = false
        get() = revoluteJoint?.isLimitEnabled ?: false
        set(value) {
            val joint = revoluteJoint
            if (joint != null) {
                joint.enableLimit(value)
            } else {
                field = value
            }
        }

    var angleRad: Float = 0f
        get() = revoluteJoint?.jointAngle ?: field
        set(value) {
            if (joint == null) {
                field = value
            } else {
                throw IllegalStateException("Can't change angle after joint creation.")
            }
        }

    var angle: Float
        get() = angleRad * MathUtils.radiansToDegrees
        set(value) {
            angleRad = value * MathUtils.degreesToRadians
        }

    var lowerLimitRad: Float = 0f
        get() = revoluteJoint?.lowerLimit ?: field
        set(value) {
            val joint = revoluteJoint
            if (joint != null) {
                joint.setLimits(value, joint.upperLimit)
            } else {
                field = value
            }
        }

    var lowerLimit: Float
        get() = lowerLimitRad * MathUtils.radiansToDegrees
        set(value) {
            lowerLimitRad = value * MathUtils.degreesToRadians
        }

    var upperLimitRad: Float = 0f
        get() = revoluteJoint?.upperLimit ?: field
        set(value) {
            val joint = revoluteJoint
            if (joint != null) {
                joint.setLimits(joint.lowerLimit, value)
            } else {
                field = value
            }
        }

    var upperLimit: Float
        get() = upperLimitRad * MathUtils.radiansToDegrees
        set(value) {
            upperLimitRad = value * MathUtils.degreesToRadians
        }

    var maxTorque: Float = 0f
        get() = revoluteJoint?.maxMotorTorque ?: field
        set(value) {
            val joint = revoluteJoint
            if (joint != null) {
                joint.maxMotorTorque = value
            } else {
                field = value
            }
        }

    var motorSpeedRad: Float = 0f
        get() = revoluteJoint?.motorSpeed ?: field
        set(value) {
            val joint = revoluteJoint
            if (joint != null) {
                joint.motorSpeed = value
            } else {
                field = value
            }
        }

    var motorSpeed: Float
        get() = motorSpeedRad * MathUtils.radiansToDegrees
        set(value) {
            motorSpeedRad = value * MathUtils.degreesToRadians
        }

    override fun initialise(box2dWorld: World) {
        joint = box2dWorld.createJoint(def)
        revoluteJoint = joint as RevoluteJoint
    }

    override fun store(box2dWorld: World) {
        super.store(box2dWorld)
        revoluteJoint = null
    }

    private val def: RevoluteJointDef
        get() {
            val def = RevoluteJointDef()
            def.bodyA = bodyA!!.body!!
            def.bodyB = bodyB!!.body!!
            def.localAnchorA.set(anchorAX, anchorAY)
            def.localAnchorB.set(anchorBX, anchorBY)
            def.collideConnected = collideConected

            def.enableMotor = isMoterEnabled
            def.enableLimit = isLimitEnabled
            def.referenceAngle = angleRad
            def.lowerAngle = lowerLimitRad
            def.upperAngle = upperLimitRad
            def.maxMotorTorque = maxTorque
            def.motorSpeed = motorSpeedRad

            return def
        }
}
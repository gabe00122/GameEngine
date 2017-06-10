package gabek.engine.core.physics.joint

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import gabek.engine.core.physics.RWorld
import gabek.engine.core.physics.TEMP_PRISMATIC_DEF

/**
 * @author Gabriel Keith
 */


class RPrismaticJoint : RJoint(), Motorized{
    private var prismaticJoint: PrismaticJoint? = null

    val localAxisA = Vector2()

    //fun setLocalAxisA(x: Float, y: Float){
    //    localAxisA.set(x, y)
    //    prismaticJoint?.localAxisA
    //}

    //fun setLocalAxisA(v: Vector2){
    //    localAxisA.set(v)
    //}

    var isLimitEnabled: Boolean = false
        set(value) {
            field = value
            prismaticJoint?.enableLimit(value)
        }

    var referenceAngle: Float
        get() = referenceAngleRad * MathUtils.radiansToDegrees
        set(value) {
            referenceAngleRad = value * MathUtils.degreesToRadians
        }

    var referenceAngleRad: Float = 0f
        set(value) {
            field = value
            if(prismaticJoint != null){
                throw IllegalStateException("Can't set reference angle after joint is initialized.")
            }
        }

    private var _upperTranslation: Float = 0f
    private var _lowerTranslation: Float = 0f

    var upperTranslation: Float
        get() = _upperTranslation
        set(value) {
            _upperTranslation = value
            prismaticJoint?.setLimits(lowerTranslation, value)
        }
    var lowerTranslation: Float
        get() = _lowerTranslation
        set(value) {
            _lowerTranslation = value
            prismaticJoint?.setLimits(value, upperTranslation)
        }

    fun setLimits(lower: Float, upper: Float){
        _lowerTranslation = lower
        _upperTranslation = upper
        prismaticJoint?.setLimits(lower, upper)
    }

    override var isMotorEnabled: Boolean = false
        set(value) {
            field = value
            prismaticJoint?.enableMotor(value)
        }

    override var motorSpeed: Float = 0f
        set(value) {
            field = value
            prismaticJoint?.motorSpeed = value
        }

    var maxMotorForce: Float = 0f
        set(value) {
            field = value
            prismaticJoint?.maxMotorForce = value
        }

    override fun update() {
    }

    override fun initialise() {
        prismaticJoint = world!!.createJoint(def) as PrismaticJoint
        joint = prismaticJoint
    }

    override fun store() {
        val joint = prismaticJoint
        prismaticJoint = null
        this.joint = null

        if(joint != null){
            world?.destroyJoint(joint)
            world = null
        }
    }

    private val def: PrismaticJointDef
        get() {
            val def = TEMP_PRISMATIC_DEF
            def.bodyA = bodyA!!.body!!
            def.bodyB = bodyB!!.body!!
            def.localAnchorA.set(anchorA)
            def.localAnchorB.set(anchorB)
            def.collideConnected = collideConnected

            def.localAxisA.set(localAxisA)

            def.enableLimit = isLimitEnabled
            def.referenceAngle = referenceAngleRad
            def.upperTranslation = upperTranslation
            def.lowerTranslation = lowerTranslation

            def.enableMotor = isMotorEnabled
            def.motorSpeed = motorSpeed
            def.maxMotorForce = maxMotorForce

            return def
        }

    override fun clone(): RJoint {
        val clonedJoint = RPrismaticJoint()
        clonedJoint.collideConnected = collideConnected

        clonedJoint.localAxisA.set(localAxisA)
        clonedJoint.isLimitEnabled = isLimitEnabled
        clonedJoint.referenceAngleRad = referenceAngleRad
        clonedJoint.upperTranslation = upperTranslation
        clonedJoint.lowerTranslation = lowerTranslation

        clonedJoint.isMotorEnabled = isMotorEnabled
        clonedJoint.motorSpeed = motorSpeed
        clonedJoint.maxMotorForce = maxMotorForce

        return clonedJoint
    }
}

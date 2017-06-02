package gabek.engine.core.physics.joint

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import gabek.engine.core.physics.TEMP_PRISMATIC_DEF

/**
 * @author Gabriel Keith
 */

/*
class RPrismaticJoint : RJoint(){
    private var prismaticJoint: PrismaticJoint? = null

    var localAxisAX: Float = 1f
        set(value) {

        }

    override fun initialise(box2dWorld: World) {
        prismaticJoint = box2dWorld.createJoint(def) as PrismaticJoint
        joint = prismaticJoint
    }

    override fun store(box2dWorld: World) {
        val joint = prismaticJoint
        prismaticJoint = null
        this.joint = null

        if(joint != null){

            box2dWorld.destroyJoint(joint)
        }
    }

    private val def: PrismaticJointDef
        get() {
            val def = TEMP_PRISMATIC_DEF
            def.bodyA = bodyA!!.body!!
            def.bodyB = bodyB!!.body!!
            def.localAnchorA.set(anchorAX, anchorAY)
            def.localAnchorB.set(anchorBX, anchorBY)
            def.collideConnected = collideConnected

            def.localAxisA

            def.enableLimit
            def.referenceAngle
            def.upperTranslation
            def.lowerTranslation

            def.enableMotor
            def.motorSpeed
            def.maxMotorForce

            return def
        }

    override fun clone(): RJoint {

    }
}
*/

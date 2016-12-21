package gabek.sm2.physics.joints

import com.badlogic.gdx.physics.box2d.Joint
import com.badlogic.gdx.physics.box2d.World
import gabek.sm2.physics.RBody

/**
 * @author Gabriel Keith
 */
abstract class RJoint{
    protected var joint: Joint? = null
    var bodyA: RBody? = null
        set(value) {
            if(isInitialised) throw IllegalStateException("Joint can't be changed after creation")

            field?.joints?.remove(this)
            value?.joints?.add(this)
            field = value
        }

    var bodyB: RBody? = null
        set(value) {
            if(isInitialised) throw IllegalStateException("Joint can't be changed after creation")

            field?.joints?.remove(this)
            value?.joints?.add(this)
            field = value
        }

    var anchorAX: Float = 0f
        set(value) {
            if(joint == null){
                field = value
            } else {
                throw IllegalStateException("Joint can't be changed after creation")
            }
        }

    var anchorAY: Float = 0f
        set(value) {
            if(joint == null){
                field = value
            } else {
                throw IllegalStateException("Joint can't be changed after creation")
            }
        }

    var anchorBX: Float = 0f
        set(value) {
            if(joint == null){
                field = value
            } else {
                throw IllegalStateException("Joint can't be changed after creation")
            }
        }

    var anchorBY: Float = 0f
        set(value) {
            if(joint == null){
                field = value
            } else {
                throw IllegalStateException("Joint can't be changed after creation")
            }
        }

    var collideConected: Boolean = false
        set(value) {
            if(joint == null){
                field = value
            } else {
                throw IllegalStateException("Joint can't be changed after creation")
            }
        }

    val canInit: Boolean
        get() {
            val bodyA = bodyA
            val bodyB = bodyB
            return bodyA != null && bodyB != null && bodyA.isInitialised && bodyB.isInitialised
        }

    val isInitialised: Boolean
        get() = joint != null

    abstract fun initialise(box2dWorld: World)

    fun attach(box2dWorld: World, bodyA: RBody, bodyB: RBody){
        this.bodyA = bodyA
        this.bodyB = bodyB

        if(canInit){
            initialise(box2dWorld)
        }
    }

    fun detach(box2dWorld: World){
        if(isInitialised){
            store(box2dWorld)
        }

        bodyA = null
        bodyB = null
    }

    open fun store(box2dWorld: World){
        val joint = joint!!
        box2dWorld.destroyJoint(joint)
        this.joint = null
    }
}
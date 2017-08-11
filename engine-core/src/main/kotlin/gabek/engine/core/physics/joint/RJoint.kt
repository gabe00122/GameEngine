package gabek.engine.core.physics.joint

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Joint
import gabek.engine.core.physics.RBody
import gabek.engine.core.physics.RWorld
import gabek.engine.core.util.entity.Cloneable

/**
 * @author Gabriel Keith
 */
abstract class RJoint: Cloneable<RJoint> {
    protected var joint: Joint? = null
    internal var world: RWorld? = null
        set(value) {
            field = value
            if(canInit){
                initialise()
            }
        }

    var bodyA: RBody? = null
        set(value) {
            if (isInitialised) throw IllegalStateException("Joint can't be changed after creation")

            field?.joints?.remove(this)
            value?.joints?.add(this)
            field = value

            if(canInit){
                initialise()
            }
        }

    var bodyB: RBody? = null
        set(value) {
            if (isInitialised) throw IllegalStateException("Joint can't be changed after creation")

            field?.joints?.remove(this)
            value?.joints?.add(this)
            field = value

            if(canInit){
                initialise()
            }
        }

    val anchorA = Vector2()
    val anchorB = Vector2()

    fun setAnchorA(x: Float, y: Float){
        anchorA.set(x, y)

        if(isInitialised)
            throw IllegalStateException("Joint can't be changed after creation")
    }

    fun setAnchorA(v: Vector2){
        anchorA.set(v)

        if(isInitialised)
            throw IllegalStateException("Joint can't be changed after creation")
    }

    fun setAnchorB(x: Float, y: Float){
        anchorB.set(x, y)

        if(isInitialised)
            throw IllegalStateException("Joint can't be changed after creation")
    }

    fun setAnchorB(v: Vector2){
        anchorB.set(v)

        if(isInitialised)
            throw IllegalStateException("Joint can't be changed after creation")
    }

    var collideConnected: Boolean = false
        set(value) {
            field = value
            if (isInitialised) {
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

    internal abstract fun update()
    internal abstract fun initialise()
    internal abstract fun store()
}
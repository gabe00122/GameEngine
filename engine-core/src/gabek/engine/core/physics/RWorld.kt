package gabek.engine.core.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import gabek.engine.core.physics.joint.RJoint

/**
 * @author Gabriel Keith
 * @date 6/1/2017
 */

class RWorld: Disposable {
    val world = World(Vector2(0f, -9.807f), true)
    private val bodyList = Array<RBody>()
    private val jointList = Array<RJoint>()

    var velocityIterations = 8
    var positionIterations = 3

    init {
        world.setContactListener(buildContactHandler())
    }

    fun update(delta: Float){
        world.step(delta, velocityIterations, positionIterations)
        for(i in 0 .. bodyList.size-1){
            bodyList[i].update()
        }
    }

    fun addBody(body: RBody){
        body.listIndex = bodyList.size
        bodyList.add(body)

        body.initialise(this)
    }

    fun removeBody(body: RBody){
        body.store()
        val index = body.listIndex

        val replacement: RBody?
        if(index < bodyList.size - 1){
            replacement = bodyList.pop()
            replacement.listIndex = index
        } else {
            replacement = null
        }

        bodyList[index] = replacement
    }

    internal fun createBody(bodyDef: BodyDef): Body {
        return world.createBody(bodyDef)
    }

    internal fun destroyBody(body: Body) {
        world.destroyBody(body)
    }

    internal fun createJoint(jointDef: JointDef): Joint {
        return world.createJoint(jointDef)
    }

    internal fun destroyJoint(joint: Joint) {
        world.destroyJoint(joint)
    }

    override fun dispose() {
        world.dispose()
    }

    private fun buildContactHandler() = object: ContactListener {
        override fun beginContact(contact: Contact) {
            val fixtureA = contact.fixtureA.userData as RFixture
            val fixtureB = contact.fixtureB.userData as RFixture

            for (callback in fixtureA.callbackList) {
                callback.begin(contact, fixtureA, fixtureB)
            }

            for (callback in fixtureB.callbackList) {
                callback.begin(contact, fixtureB, fixtureA)
            }
        }

        override fun endContact(contact: Contact) {
            val fixtureA = contact.fixtureA.userData as RFixture
            val fixtureB = contact.fixtureB.userData as RFixture

            for (callback in fixtureA.callbackList) {
                callback.end(contact, fixtureA, fixtureB)
            }

            for (callback in fixtureB.callbackList) {
                callback.end(contact, fixtureB, fixtureA)
            }
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold) {
            val fixtureA = contact.fixtureA.userData as RFixture
            val fixtureB = contact.fixtureB.userData as RFixture
            contact.tangentSpeed = 0f


            for (callback in fixtureA.callbackList) {
                callback.preSolve(contact, oldManifold, fixtureA, fixtureB)
            }

            for (callback in fixtureB.callbackList) {
                callback.preSolve(contact, oldManifold, fixtureB, fixtureA)
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse) {
            val fixtureA = contact.fixtureA.userData as RFixture
            val fixtureB = contact.fixtureB.userData as RFixture

            for (callback in fixtureA.callbackList) {
                callback.postSolve(contact, impulse, fixtureA, fixtureB)
            }

            for (callback in fixtureB.callbackList) {
                callback.postSolve(contact, impulse, fixtureB, fixtureA)
            }
        }
    }
}
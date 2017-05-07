package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Disposable
import gabek.sm2.components.BodyCom
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.physics.RFixture
import gabek.sm2.systems.common.TranslationSystem

/**
 * @author Gabriel Keith
 */
class Box2dSystem : BaseEntitySystem(Aspect.all(BodyCom::class.java, TranslationCom::class.java)), Disposable {

    private lateinit var transSystem: TranslationSystem
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>

    val box2dWorld = World(Vector2(0f, -9.807f), true)


    override fun initialize() {
        box2dWorld.setContactListener(contactHandler)

        world.aspectSubscriptionManager.get(Aspect.all(BodyCom::class.java)).
                addSubscriptionListener(bodyInitHandler)

        transSystem.addTeleportListener(teleportHandler)
    }

    override fun processSystem() {
        box2dWorld.step(world.delta, 8, 3)

        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val bodyCom = bodyMapper.get(entity)
            val transCom = transMapper.get(entity)

            val body = bodyCom.body
            transCom.x = body.x
            transCom.y = body.y
            transCom.rotation = body.rotation
        }
    }

    override fun dispose() {
        box2dWorld.dispose()
    }

    private val bodyInitHandler = object : EntitySubscription.SubscriptionListener {
        override fun inserted(entities: IntBag) {
            for (i in 0 until entities.size()) {
                val entity = entities[i]
                val body = bodyMapper[entity].body

                body.initialise(box2dWorld, entity)
            }
        }

        override fun removed(entities: IntBag) {
            for (i in 0 until entities.size()) {
                val body = bodyMapper[entities[i]].body
                body.store(box2dWorld)
            }
        }
    }

    private val teleportHandler = object : TranslationSystem.TeleportListener {
        override fun onTeleport(id: Int, x: Float, y: Float, rotation: Float, smooth: Boolean) {
            if (bodyMapper.has(id)) {
                val body = bodyMapper[id].body
                body.setPosition(x, y)
                body.rotation = rotation
            }
        }
    }

    private val contactHandler = object : ContactListener {
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
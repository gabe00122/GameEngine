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
import gabek.sm2.components.ContactCom
import gabek.sm2.components.TranslationCom
import gabek.sm2.physics.RContact
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class Box2dSystem : BaseEntitySystem(Aspect.all(BodyCom::class.java, TranslationCom::class.java)), Disposable, ContactListener {
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var contactMapper: ComponentMapper<ContactCom>

    val box2dWorld = World(Vector2(0f, -9.807f), true)

    init {
        box2dWorld.setContactListener(this)
    }

    override fun initialize() {
        world.aspectSubscriptionManager.get(Aspect.all(BodyCom::class.java)).
                addSubscriptionListener(object : EntitySubscription.SubscriptionListener {
                    override fun inserted(entities: IntBag) {
                        for (i in 0 until entities.size()) {
                            val body = bodyMapper[entities[i]].rBody
                            body.initialise(box2dWorld, entities[i])
                        }
                    }

                    override fun removed(entities: IntBag) {
                        for (i in 0 until entities.size()) {
                            val body = bodyMapper[entities[i]].rBody
                            body.store(box2dWorld)
                        }
                    }
                })
    }

    override fun processSystem() {
        box2dWorld.step(world.delta, 8, 3)

        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val bodyCom = bodyMapper.get(entity)
            val transCom = transMapper.get(entity)

            val body = bodyCom.rBody
            transCom.x = body.x
            transCom.y = body.y
            transCom.rotation = body.rotation
        }
    }

    override fun dispose() {
        box2dWorld.dispose()
    }

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA.userData as RFixture
        val bodyA = fixtureA.body!!
        val entityA = fixtureA.colisionCallback

        val fixtureB = contact.fixtureB.userData as RFixture
        val bodyB = fixtureB.body!!
        val entityB = fixtureB.colisionCallback

        //println(entityA)
        val manifold = contact.worldManifold

        val contactPoints = arrayOf(Vector2(), Vector2())
        contactPoints[0].set(manifold.points[0])
        contactPoints[1].set(manifold.points[1])

        if (entityA != -1 && contactMapper.has(entityA)) {
            contactMapper[entityA].contacts.add(RContact(bodyA, fixtureA, entityB, bodyB, fixtureB))
        }

        if (entityB != -1 && contactMapper.has(entityB)) {
            contactMapper[entityB].contacts.add(RContact(bodyB, fixtureB, entityA, bodyA, fixtureA))
        }
    }

    override fun endContact(contact: Contact) {
        val fixtureA = contact.fixtureA.userData as RFixture
        val entityA = fixtureA.colisionCallback

        val fixtureB = contact.fixtureB.userData as RFixture
        val entityB = fixtureB.colisionCallback

        if (entityA != -1 && contactMapper.has(entityA)) {
            val contacts = contactMapper[entityA].contacts
            contacts.removeIndex(findRContactIndex(contacts, fixtureA, fixtureB))
        }

        if (entityB != -1 && contactMapper.has(entityB)) {
            val contacts = contactMapper[entityB].contacts
            contacts.removeIndex(findRContactIndex(contacts, fixtureB, fixtureA))
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        val fixtureA = contact.fixtureA.userData as RFixture
        val entityA = fixtureA.colisionCallback

        val fixtureB = contact.fixtureB.userData as RFixture
        val entityB = fixtureB.colisionCallback

        if(entityA != -1 && contactMapper.has(entityA)){
            findRContact(contactMapper[entityA].contacts, fixtureA, fixtureB).update(contact)
        }

        if(entityB != -1 && contactMapper.has(entityB)){
            findRContact(contactMapper[entityB].contacts, fixtureB, fixtureA).update(contact)
        }
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {
    }

    fun findRContactIndex(rContacts: com.badlogic.gdx.utils.Array<RContact>, fixtureA: RFixture, fixtureB: RFixture): Int{
        for (i in 0 until rContacts.size) {
            val c = rContacts[i]
            if (c.fixture === fixtureA && c.otherFixture === fixtureB) {
                return i
            }
        }
        throw IllegalStateException()
    }

    fun findRContact(rContacts: com.badlogic.gdx.utils.Array<RContact>, fixtureA: RFixture, fixtureB: RFixture): RContact
            = rContacts[findRContactIndex(rContacts, fixtureA, fixtureB)]
}
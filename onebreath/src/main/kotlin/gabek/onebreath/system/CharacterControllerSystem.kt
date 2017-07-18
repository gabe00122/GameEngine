package gabek.onebreath.system

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.EntityTransmuterFactory
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.InputCom
import gabek.engine.core.components.channel.DirectionalInputCom
import gabek.onebreath.component.CharacterMovementStateCom.State.*
import gabek.onebreath.component.BiDirectionCom.Direction.*
import gabek.engine.core.physics.RCollisionCallback
import gabek.engine.core.physics.RFixture
import gabek.engine.core.prefab.Prefab
import gabek.engine.core.systems.common.PrefabManager
import gabek.engine.core.systems.common.UpdateManager
import gabek.engine.core.util.FSMTransitionTable
import gabek.onebreath.component.*

/**
 * @author Gabriel Keith
 */
class CharacterControllerSystem: BaseEntitySystem(
        Aspect.all(
                DirectionalInputCom::class.java,
                BiDirectionCom::class.java,
                CharacterMovementStateCom::class.java,
                MovementDefinitionCom::class.java,
                MovementGroundContactCom::class.java,
                BodyCom::class.java
        )) {
    private lateinit var directionalInputMapper: ComponentMapper<DirectionalInputCom>
    private lateinit var biDirectionMapper: ComponentMapper<BiDirectionCom>
    private lateinit var movementStateMapper: ComponentMapper<CharacterMovementStateCom>
    private lateinit var movementDefinitionMapper: ComponentMapper<MovementDefinitionCom>
    private lateinit var movementGroundContactMapper: ComponentMapper<MovementGroundContactCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    private lateinit var damageSystem: DamageSystem
    private lateinit var updateManager: UpdateManager
    private lateinit var biDirectionSystem: BiDirectionSystem

    private lateinit var prefabManager: PrefabManager
    private lateinit var bloodFactory: Prefab

    private val threshold = 0.2f

    val transitionTable = FSMTransitionTable<CharacterMovementStateCom.State>(CharacterMovementStateCom.State.values().size) { entity, state ->
        //println("${movementStateMapper[entity].state} $state ${updateManager.currentFrame}")
        movementStateMapper[entity].state = state
        checkTransitions(entity)
    }

    override fun initialize() {
        transitionTable.addListenerEntering(LANDING) { entity, from, to ->
            movementStateMapper[entity].timeOnGround = updateManager.currentFrame
        }

        transitionTable.addListener(arrayOf(RUNNING, STANDING), arrayOf(JUMPING, IN_AIR)) { entity, from, to ->
            movementStateMapper[entity].timeInAir = updateManager.currentFrame
        }

        transitionTable.addListenerEntering(JUMPING) { entity, from, to ->
            val body = bodyMapper[entity].body
            val movContact = movementGroundContactMapper[entity]
            val movDef = movementDefinitionMapper[entity]

            val force = movDef.jumpForce
            body.applyLinearImpulse(0f, force, body.position.x, body.position.y)

            val forceOverSize = force / movContact.contacts.size
            for (contact in movContact.contacts) {
                val otherBody = contact.fixture.body!!
                for (i in 0 until contact.numberOfPoints) {
                    otherBody.applyLinearImpulse(0f,
                            -forceOverSize / contact.numberOfPoints,
                            contact.points[i].x, contact.points[i].y)
                }
            }
        }

        bloodFactory = prefabManager.getPrefab("blood")

        val transmuter = EntityTransmuterFactory(world)
                .remove(CharacterControllerCom::class.java)
                .remove(InputCom::class.java)
                .build()

        damageSystem.addDeathListener { entity: Int, damage: Float, damageType: Int ->
            if (directionalInputMapper.has(entity) && bodyMapper.has(entity)) {
                transmuter.transmute(entity)
                val body = bodyMapper[entity].body

                body.isFixedRotation = false
                body.angularVelocity = if (body.linearVelocity.x > 0) -10f else 10f
                body.fixutres[0].callbackList.clear()

                for (i in 0 until 25) {
                    bloodFactory.create(body.position.x, body.position.y)
                }
            }
        }
        //addTransitionListener(RUNNING, ON_GROUND)
    }

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val input = directionalInputMapper[entity]
            val biDirection = biDirectionMapper[entity]
            val movState = movementStateMapper[entity]
            val moveDef = movementDefinitionMapper[entity]
            val body = bodyMapper[entity]


            //transitions
            when (biDirection.direction) {
                LEFT -> {
                    if (input.panX > threshold) {
                        biDirectionSystem.setDirection(entity, RIGHT)
                    }
                }
                RIGHT -> {
                    if (input.panX < -threshold) {
                        biDirectionSystem.setDirection(entity, LEFT)
                    }
                }
            }
            checkTransitions(entity)

            //physics
            when (movState.state) {
                RUNNING -> {
                    body.body.isAwake = true
                }
                JUMPING, IN_AIR -> {
                    val linearVelocityX = body.body.linearVelocity.x
                    if (input.panX < -threshold && linearVelocityX > -moveDef.airSpeed) {
                        body.body.applyForceToCenter(-5f, 0f)
                    } else if (input.panX > threshold && linearVelocityX < moveDef.airSpeed) {
                        body.body.applyForceToCenter(5f, 0f)
                    }

                    val damp = -body.body.linearVelocity.x * body.body.mass * moveDef.airDamping

                    body.body.applyForceToCenter(damp, 0f, false)
                }
                else -> {
                }
            }


        }
    }

    private fun checkTransitions(entity: Int) {
        val input = directionalInputMapper[entity]
        val movState = movementStateMapper[entity]
        val movContact = movementGroundContactMapper[entity]

        val timeOnGround = updateManager.getElapsedTime(movState.timeOnGround)
        val timeInAir = updateManager.getElapsedTime(movState.timeInAir)

        when (movState.state) {
            LANDING -> {
                if (Math.abs(input.panX) > threshold) {
                    transitionTable.transition(entity, LANDING, RUNNING)
                } else {
                    transitionTable.transition(entity, LANDING, STANDING)
                }
            }
            STANDING -> {
                if (!movContact.onGround) {
                    transitionTable.transition(entity, STANDING, IN_AIR)
                } else if(input.panY > threshold && timeOnGround > 0.1f) {
                    transitionTable.transition(entity, STANDING, JUMPING)
                } else if(Math.abs(input.panX) > threshold) {
                    transitionTable.transition(entity, STANDING, RUNNING)
                }
            }
            RUNNING -> {
                if(!movContact.onGround) {
                    transitionTable.transition(entity, RUNNING, IN_AIR)
                } else if(input.panY > threshold && timeOnGround > 0.1f) {
                    transitionTable.transition(entity, RUNNING, JUMPING)
                } else if(Math.abs(input.panX) < threshold) {
                    transitionTable.transition(entity, RUNNING, STANDING)
                }
            }
            IN_AIR -> {
                if (movContact.onGround && timeInAir > 0.1f) {
                    transitionTable.transition(entity, IN_AIR, LANDING)
                }
            }
            JUMPING -> {
                if (movContact.onGround && timeInAir > 0.1f) {
                    transitionTable.transition(entity, JUMPING, LANDING)
                }
            }
        }
    }

    override fun inserted(entityId: Int) {
        val groundContact = movementGroundContactMapper[entityId]

        bodyMapper[entityId].body.fixutres[groundContact.platformIndex].callbackList.add(platformContactHandler)
    }

    private val platformContactHandler = object: RCollisionCallback {
        private val normal = Vector2()

        override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {}

        override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val contacts = movementGroundContactMapper[ownerRFixture.ownerId]
            contacts.remove(otherRFixture)
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val contacts = movementGroundContactMapper[ownerRFixture.ownerId]
            val input = directionalInputMapper[ownerRFixture.ownerId]
            val def = movementDefinitionMapper[ownerRFixture.ownerId]

            val manifold = contact.worldManifold
            normal.set(manifold.normal)

            if (contact.fixtureA.userData !== ownerRFixture) {
                normal.x = -normal.x
                normal.y = -normal.y
            }
            normal.set(ownerRFixture.body!!.getLocalVector(normal))
            val angle = normal.angle()

            if (contacts.platformMinAngle < angle && angle < contacts.platformMaxAngle) {
                contact.tangentSpeed += def.groundSpeed * input.panX
                contact.friction = 1f

                val groundContact = contacts.getOrCreate(otherRFixture)
                groundContact.normal.set(normal)
                groundContact.numberOfPoints = manifold.numberOfContactPoints
                for (i in 0 until manifold.numberOfContactPoints) {
                    groundContact.points[i].set(manifold.points[i])
                }
            } else {
                contacts.remove(ownerRFixture)
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {}
    }
}
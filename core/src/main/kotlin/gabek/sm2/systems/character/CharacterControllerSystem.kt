package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold
import gabek.sm2.components.BodyCom
import gabek.sm2.components.character.*
import gabek.sm2.components.character.BiDirectionCom.Direction.LEFT
import gabek.sm2.components.character.BiDirectionCom.Direction.RIGHT
import gabek.sm2.components.character.CharacterMovementStateCom.State.*
import gabek.sm2.physics.RCollisionCallback
import gabek.sm2.physics.RFixture
import gabek.sm2.systems.ParentSystem
import gabek.sm2.systems.TimeManager
import gabek.sm2.systems.TranslationSystem
import gabek.sm2.util.FSMTransitionTable

/**
 * @author Gabriel Keith
 */
class CharacterControllerSystem : BaseEntitySystem(
        Aspect.all(
                CharacterControllerCom::class.java,
                BiDirectionCom::class.java,
                CharacterMovementStateCom::class.java,
                MovementDefinitionCom::class.java,
                MovementGroundContactCom::class.java,
                BodyCom::class.java
        )) {
    private lateinit var controllerMapper: ComponentMapper<CharacterControllerCom>
    private lateinit var biDirectionMapper: ComponentMapper<BiDirectionCom>
    private lateinit var movementStateMapper: ComponentMapper<CharacterMovementStateCom>
    private lateinit var movementDefinitionMapper: ComponentMapper<MovementDefinitionCom>
    private lateinit var movementGroundContactMapper: ComponentMapper<MovementGroundContactCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    private lateinit var timeManager: TimeManager
    private lateinit var parentSystem: ParentSystem
    private lateinit var transSystem: TranslationSystem
    private lateinit var biDirectionSystem: BiDirectionSystem

    val transitionTable = FSMTransitionTable(CharacterMovementStateCom.State::class) { entity, state ->
        println("${movementStateMapper[entity].state} $state ${timeManager.currentFrame}")

        movementStateMapper[entity].state = state
        checkTransitions(entity)
    }

    init {
        transitionTable.addListenerEntering(LANDING) { entity, from, to ->
            movementStateMapper[entity].timeOnGround = timeManager.currentFrame
        }

        transitionTable.addListener(arrayOf(RUNNING, STANDING), arrayOf(JUMPING, IN_AIR)) { entity, from, to ->
            movementStateMapper[entity].timeInAir = timeManager.currentFrame
        }

        transitionTable.addListenerEntering(JUMPING) { entity, from, to ->
            val body = bodyMapper[entity].body
            val movContact = movementGroundContactMapper[entity]
            val movDef = movementDefinitionMapper[entity]

            val force = movDef.jumpForce
            body.applyLinearImpulse(0f, force, body.x, body.y)

            val forceOverSize = force / movContact.contacts.size
            for (contact in movContact.contacts) {
                val otherBody = contact.fixture.body!!
                for(i in 0 until contact.numberOfPoints) {
                    val point = contact.points[i]
                    otherBody.applyLinearImpulse(0f, -forceOverSize / contact.numberOfPoints, point.x, point.y)
                }
            }
        }

        //addTransitionListener(RUNNING, ON_GROUND)
    }

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val control = controllerMapper[entity]
            val biDirection = biDirectionMapper[entity]
            val movState = movementStateMapper[entity]
            val moveDef = movementDefinitionMapper[entity]
            val body = bodyMapper[entity]


            //transitions
            when (biDirection.direction) {
                LEFT -> {
                    if (control.moveRight && !control.moveLeft) {
                        biDirectionSystem.setDirection(entity, RIGHT)
                    }
                }
                RIGHT -> {
                    if (control.moveLeft && !control.moveRight) {
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
                JUMPING -> {
                    val linearVelocityX = body.body.linearVelocityX
                    if (control.moveLeft && linearVelocityX > -moveDef.airSpeed) {
                        body.body.applyForceToCenter(-20f, 0f)
                    } else if (control.moveRight && linearVelocityX < moveDef.airSpeed) {
                        body.body.applyForceToCenter(20f, 0f)
                    }

                    val damp = -body.body.linearVelocityX * body.body.mass * moveDef.airDamping
                    body.body.applyForceToCenter(damp, 0f, false)
                }
                else -> {
                }
            }


        }
    }

    private fun checkTransitions(entity: Int) {
        val control = controllerMapper[entity]
        val movState = movementStateMapper[entity]
        val movContact = movementGroundContactMapper[entity]

        val timeOnGround = timeManager.getElapsedTime(movState.timeOnGround)
        val timeInAir = timeManager.getElapsedTime(movState.timeInAir)

        when (movState.state) {
            LANDING -> {
                if (control.moveLeft || control.moveRight) {
                    transitionTable.transition(entity, LANDING, RUNNING)
                } else {
                    transitionTable.transition(entity, LANDING, STANDING)
                }
            }

            STANDING -> {
                if (!movContact.onGround) {
                    transitionTable.transition(entity, STANDING, IN_AIR)
                } else if (control.moveUp && timeOnGround > 0.1f) {
                    transitionTable.transition(entity, STANDING, JUMPING)
                } else if (control.moveLeft || control.moveRight) {
                    transitionTable.transition(entity, STANDING, RUNNING)
                }
            }
            RUNNING -> {
                if (!movContact.onGround) {
                    transitionTable.transition(entity, RUNNING, IN_AIR)
                } else if (control.moveUp && timeOnGround > 0.1f) {
                    transitionTable.transition(entity, RUNNING, JUMPING)
                } else if (!control.moveLeft && !control.moveRight) {
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

    private val platformContactHandler = object : RCollisionCallback {
        override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val contacts = movementGroundContactMapper[ownerRFixture.ownerId]

            val index = contacts.indexOf(otherRFixture)
            if (index == -1) {
                contacts.add(otherRFixture)
            }
        }

        override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val contacts = movementGroundContactMapper[ownerRFixture.ownerId]
            contacts.remove(otherRFixture)
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val contacts = movementGroundContactMapper[ownerRFixture.ownerId]
            val control = controllerMapper[ownerRFixture.ownerId]
            val def = movementDefinitionMapper[ownerRFixture.ownerId]

            contact.tangentSpeed += def.groundSpeed * control.lateralMovement

            val groundContact = contacts.get(otherRFixture)
            if(groundContact != null){
                val manifold = contact.worldManifold
                groundContact.numberOfPoints = manifold.numberOfContactPoints
                for(i in 0 until manifold.numberOfContactPoints){
                    groundContact.points[i].set(manifold.points[i])
                }
            }
        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {}
    }
}
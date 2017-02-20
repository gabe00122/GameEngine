package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.Manifold
import gabek.sm2.components.BodyCom
import gabek.sm2.components.ParentOfCom
import gabek.sm2.components.character.*
import gabek.sm2.components.character.BiDirectionCom.Direction.LEFT
import gabek.sm2.components.character.BiDirectionCom.Direction.RIGHT
import gabek.sm2.components.character.CharacterMovementStateCom.State.*
import gabek.sm2.physics.RBody
import gabek.sm2.physics.RCollisionCallback
import gabek.sm2.physics.RContact
import gabek.sm2.physics.RFixture
import gabek.sm2.systems.BiDirectionSystem
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
                MovementPhysicsCom::class.java,
                BodyCom::class.java
        )) {
    private lateinit var parentMapper: ComponentMapper<ParentOfCom>
    private lateinit var controllerMapper: ComponentMapper<CharacterControllerCom>
    private lateinit var biDirectionMapper: ComponentMapper<BiDirectionCom>
    private lateinit var movementStateMapper: ComponentMapper<CharacterMovementStateCom>
    private lateinit var movementDefinitionMapper: ComponentMapper<MovementDefinitionCom>
    private lateinit var movementPhysicsMapper: ComponentMapper<MovementPhysicsCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>

    private lateinit var timeManager: TimeManager
    private lateinit var parentSystem: ParentSystem
    private lateinit var transSystem: TranslationSystem
    private lateinit var biDirectionSystem: BiDirectionSystem

    val transitionTable = FSMTransitionTable(CharacterMovementStateCom.State::class)
    { entity, state ->
        val movState = movementStateMapper[entity]
        movState.state = state
        movState.timeInState = timeManager.currentFrame
        checkTransitions(entity)
    }

    init {
        transitionTable.addListenerLeavening(RUNNING) { entity, from, to ->
            movementPhysicsMapper[entity].motor.motorSpeed = 0f
        }

        transitionTable.addListenerEntering(JUMPING) { entity, from, to ->
            val body = bodyMapper[entity].body
            val movPhys = movementPhysicsMapper[entity]
            val movDef = movementDefinitionMapper[entity]

            val force = movDef.jumpForce
            val groundBody = movPhys.groundFixture!!.body!!
            val point = movPhys.groundPoint

            body.applyLinearImpulse(0f, force, body.x, body.y)
            groundBody.applyLinearImpulse(0f, -force, point.x, point.y)
        }

        //addTransitionListener(RUNNING, ON_GROUND)
    }

    override fun initialize() {
        super.initialize()
        transSystem.addTeleportListener(object : TranslationSystem.TeleportListener {
            override fun onTeleport(id: Int, x: Float, y: Float, rotation: Float, smooth: Boolean) {
                if (movementPhysicsMapper.has(id)) {
                    transSystem.teleportChild(id, movementPhysicsMapper[id].wheelRef, x, y, rotation, smooth)
                }
            }
        })
    }

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val control = controllerMapper[entity]
            val biDirection = biDirectionMapper[entity]
            val movState = movementStateMapper[entity]
            val moveDef = movementDefinitionMapper[entity]
            val movPhysics = movementPhysicsMapper[entity]
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
                    if (control.moveLeft) {
                        movPhysics.motor.motorSpeed = moveDef.groundSpeed
                    } else if (control.moveRight) {
                        movPhysics.motor.motorSpeed = -moveDef.groundSpeed
                    }
                }
                JUMPING -> {
                    val linearVelocityX = body.body.linearVelocityX
                    if (control.moveLeft && linearVelocityX > -4) {
                        body.body.applyForceToCenter(-moveDef.airSpeed * world.delta, 0f)
                    } else if (control.moveRight && linearVelocityX < 4) {
                        body.body.applyForceToCenter(moveDef.airSpeed * world.delta, 0f)
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
        val movPhysics = movementPhysicsMapper[entity]

        val timeInState = timeManager.getElapsedTime(movState.timeInState)

        when (movState.state) {
            LANDING -> {
                if(control.moveLeft || control.moveRight){
                    transitionTable.transition(entity, LANDING, RUNNING)
                } else {
                    transitionTable.transition(entity, LANDING, STANDING)
                }
            }

            STANDING -> {
                if (control.moveLeft || control.moveRight) {
                    transitionTable.transition(entity, STANDING, RUNNING)
                }
                if (control.moveUp && timeInState > 0.2f) {
                    transitionTable.transition(entity, STANDING, JUMPING)
                }
            }
            RUNNING -> {
                if (!control.moveLeft && !control.moveRight) {
                    transitionTable.transition(entity, RUNNING, STANDING)
                }
                if (control.moveUp && timeInState > 0.2f) {
                    transitionTable.transition(entity, RUNNING, JUMPING)
                }
            }
            JUMPING -> {
                if (movPhysics.groundFixture != null && timeInState > 0.2f) {
                    transitionTable.transition(entity, JUMPING, LANDING)
                }
            }
            else -> {
            }
        }
    }

    override fun inserted(entityId: Int) {
        val physics = movementPhysicsMapper[entityId]

        bodyMapper[physics.wheelRef].body.fixutres[0].callbackList.add(contactHandler)
    }

    private val contactHandler = object : RCollisionCallback {
        override fun begin(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {

        }

        override fun end(contact: Contact, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val entity = parentSystem.getParent(ownerRFixture.ownerId)
            movementPhysicsMapper[entity].groundFixture = null
        }

        override fun preSolve(contact: Contact, oldManifold: Manifold, ownerRFixture: RFixture, otherRFixture: RFixture) {

        }

        override fun postSolve(contact: Contact, impulse: ContactImpulse, ownerRFixture: RFixture, otherRFixture: RFixture) {
            val entity = parentSystem.getParent(ownerRFixture.ownerId)
            val movPhysics = movementPhysicsMapper[entity]
            val movDef = movementDefinitionMapper[entity]

            val hWidth = movDef.width / 2f
            val point = contact.worldManifold.points[0]
            val diffX = point.x - ownerRFixture.body!!.x

            if(diffX < hWidth - movDef.pad && diffX > -hWidth + movDef.pad) {
                movPhysics.groundFixture = otherRFixture
                movPhysics.groundPoint.set(point)
            } else {
                movPhysics.groundFixture = null
            }
        }
    }
}
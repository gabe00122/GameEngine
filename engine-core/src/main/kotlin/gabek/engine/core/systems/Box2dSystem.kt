package gabek.engine.core.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.EntitySubscription
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Disposable
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.physics.RFixture
import gabek.engine.core.physics.RWorld
import gabek.engine.core.systems.common.TranslationSystem

/**
 * @author Gabriel Keith
 */
class Box2dSystem: BaseEntitySystem(Aspect.all(BodyCom::class.java, TranslationCom::class.java)), Disposable {
    private lateinit var transSystem: TranslationSystem
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>

    val rworld = RWorld()

    var onBodyInit: ((entityId: Int, body: Body) -> Unit)? = null //temp
    var onBodyStore: ((entityId: Int, body: Body) -> Unit)? = null //temp

    override fun initialize() {
        world.aspectSubscriptionManager.get(Aspect.all(BodyCom::class.java)).
                addSubscriptionListener(bodyInitHandler)

        //transSystem.addTeleportListener(teleportHandler)
    }

    override fun processSystem() {
        transToBodySync()
        rworld.update(world.delta)
        bodyToTransSync()
    }

    private fun transToBodySync(){
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val bodyCom = bodyMapper.get(entity)
            val transCom = transMapper.get(entity)

            val body = bodyCom.body
            if(transCom.x != body.position.x || transCom.y != body.position.y){
                body.setPosition(transCom.x, transCom.y)
            }
            if(transCom.rotation != body.rotation) {
                println("test")
                body.rotation = transCom.rotation
            }
        }
    }

    private fun bodyToTransSync(){
        val entities = entityIds
        for (i in 0 until entities.size()) {
            val entity = entities[i]
            val bodyCom = bodyMapper.get(entity)
            val transCom = transMapper.get(entity)

            val body = bodyCom.body
            transCom.x = body.position.x
            transCom.y = body.position.y
            transCom.rotation = body.rotation
        }
    }

    override fun dispose() {
        rworld.dispose()
    }

    private val bodyInitHandler = object: EntitySubscription.SubscriptionListener {
        override fun inserted(entities: IntBag) {
            for (i in 0 until entities.size()) {
                val entity = entities[i]
                val body = bodyMapper[entity].body

                rworld.addBody(body)
                for(j in 0 until body.fixutres.size){
                    body.fixutres[j].ownerId = entity
                }

                onBodyInit?.invoke(entity, body.body!!)
            }
        }

        override fun removed(entities: IntBag) {
            for (i in 0 until entities.size()) {
                val entityId = entities[i]

                val body = bodyMapper[entityId].body
                onBodyStore?.invoke(entityId, body.body!!)

                rworld.removeBody(body)
            }
        }
    }

    private val teleportHandler = object: TranslationSystem.TeleportListener {
        override fun onTeleport(id: Int, x: Float, y: Float, rotation: Float, smooth: Boolean) {
            if (bodyMapper.has(id)) {
                val body = bodyMapper[id].body
                body.setPosition(x, y)
                body.rotation = rotation
            }
        }
    }
}
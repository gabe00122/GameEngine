package gabek.sm2.systems.pellet

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.physics.box2d.Fixture
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.components.pellet.PelletMovementCom
import gabek.sm2.systems.Box2dSystem

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class PelletMovmentSystem: BaseEntitySystem(Aspect.all(TranslationCom::class.java, PelletMovementCom::class.java)){
    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var pelletMovementMapper: ComponentMapper<PelletMovementCom>
    private lateinit var box2dWorld: Box2dSystem


    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]

            val trans = transMapper[entity]
            val movement = pelletMovementMapper[entity]

            movement.speedY -= movement.gravity * world.delta

            val nextX = trans.x + movement.speedX * world.delta
            val nextY = trans.y + movement.speedY * world.delta

            var hit = false
            box2dWorld.box2dWorld.rayCast({ fixture, point, normal, fraction ->
                hit = true
                1f
            }, trans.x, trans.y, nextX, nextY)

            if(hit) {
                movement.speedX = -movement.speedX * 0.15f
                movement.speedY = -movement.speedY * 0.15f
            }

            trans.x += movement.speedX * world.delta
            trans.y += movement.speedY * world.delta
        }
    }

}
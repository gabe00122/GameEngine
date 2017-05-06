package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.StaticJointCom
import gabek.sm2.physics.RBody
import gabek.sm2.systems.common.TranslationSystem

/**
 * @author Gabriel Keith
 * @date 3/31/2017
 */
class StaticJointSystem : BaseEntitySystem(Aspect.all(StaticJointCom::class.java)) {
    private lateinit var box2dSystem: Box2dSystem
    private lateinit var staticJointMapper: ComponentMapper<StaticJointCom>
    private lateinit var translationSystem: TranslationSystem

    private val baseBody = RBody()

    override fun initialize() {
        super.initialize()
        baseBody.initialise(box2dSystem.box2dWorld)

        translationSystem.addTeleportListener(object : TranslationSystem.TeleportListener {
            override fun onTeleport(id: Int, x: Float, y: Float, rotation: Float, smooth: Boolean) {
                if (staticJointMapper.has(id)) {
                    val joint = staticJointMapper[id].joint
                    if (joint != null) {
                        joint.anchorBX = x
                        joint.anchorBY = y
                    }
                }
            }
        })
    }

    override fun processSystem() {}

    override fun inserted(entityId: Int) {
        super.inserted(entityId)
        val joint = staticJointMapper[entityId].joint

        if (joint != null) {
            joint.bodyB = baseBody
        }
    }
}
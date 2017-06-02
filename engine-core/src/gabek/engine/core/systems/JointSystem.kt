package gabek.engine.core.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.BodyCom
import gabek.engine.core.components.JointCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.physics.RBody
import gabek.engine.core.systems.common.TranslationSystem

/**
 * @author Gabriel Keith
 * @date 3/31/2017
 */
class JointSystem : BaseEntitySystem(Aspect.all(JointCom::class.java, BodyCom::class.java, TranslationCom::class.java)) {
    private lateinit var box2dSystem: Box2dSystem
    private lateinit var jointMapper: ComponentMapper<JointCom>
    private lateinit var bodyMapper: ComponentMapper<BodyCom>
    private lateinit var transMapper: ComponentMapper<TranslationCom>

    private val baseBody = RBody()

    override fun initialize() {
        super.initialize()
        box2dSystem.rworld.addBody(baseBody)
    }

    override fun processSystem() {}

    override fun inserted(entityId: Int) {
        super.inserted(entityId)
        val jointCom = jointMapper[entityId]
        val bodyCom = bodyMapper[entityId]
        val transCom = transMapper[entityId]

        val joint = jointCom.joint

        if (joint != null) {
            if(jointCom.sourceType == JointCom.SourceType.SELF){
                joint.bodyA = bodyCom.body
            } else if(jointCom.srcId != -1){
                joint.bodyA = bodyMapper[jointCom.srcId].body
            }

            if(jointCom.destinationType == JointCom.DestinationType.STATIC){
                joint.bodyB = baseBody
                joint.setAnchorB(transCom.x, transCom.y)
            } else if(jointCom.destId != -1){
                joint.bodyB = bodyMapper[jointCom.destId].body
            }


            println("test")
            if(joint.canInit) {
                joint.initialise(box2dSystem.rworld)
            }
        }
    }
}
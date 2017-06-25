package gabek.onebreath.system

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.JointCom
import gabek.engine.core.physics.joint.Motorized
import gabek.engine.core.systems.common.UpdateManager
import gabek.onebreath.component.MotorOscillatorCom

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */

class MotorOscillatorSystem: BaseEntitySystem(
        Aspect.all(
                JointCom::class.java,
                MotorOscillatorCom::class.java
        )) {
    private lateinit var updateManager: UpdateManager

    private lateinit var jointMapper: ComponentMapper<JointCom>
    private lateinit var oscillatorMapper: ComponentMapper<MotorOscillatorCom>

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 .. entities.size() - 1){
            val entity = entities[i]
            val joint = jointMapper[entity].joint
            val oscillator = oscillatorMapper[entity]


            if(joint != null && joint is Motorized
                    && updateManager.currentTime > oscillator.target) {
                joint.reverseDirection()
                oscillator.target = updateManager.currentTime + 5f
            }
        }
    }
}
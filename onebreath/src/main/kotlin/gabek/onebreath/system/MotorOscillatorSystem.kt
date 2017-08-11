package gabek.onebreath.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import gabek.engine.core.components.JointCom
import gabek.engine.core.physics.joint.Motorized
import gabek.engine.core.systems.common.TimerUpdateSystem
import gabek.engine.core.systems.common.UpdateManager
import gabek.onebreath.component.MotorOscillatorCom

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */

class MotorOscillatorSystem: TimerUpdateSystem(
        Aspect.all(
                JointCom::class.java,
                MotorOscillatorCom::class.java
        )) {
    private lateinit var updateManager: UpdateManager

    private lateinit var jointMapper: ComponentMapper<JointCom>
    //private lateinit var oscillatorMapper: ComponentMapper<MotorOscillatorCom>

    override fun inserted(entityId: Int) {
        super.inserted(entityId)
        onCheck(entityId)
    }

    override fun onCheck(entity: Int){
        val joint = jointMapper[entity].joint
        //val oscillator = oscillatorMapper[entity]

        if(joint != null && joint is Motorized) {
            joint.reverseDirection()
            //oscillator.target = updateManager.currentTime + 5f

            checkAt(entity, updateManager.currentTime + 5f)
        }
    }


}
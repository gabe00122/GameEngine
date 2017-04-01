package gabek.sm2.components.pellet

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.PooledComponent

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class PelletMovementCom: PooledComponent(){
    var gravity: Float = 0f
    var speedX: Float = 0f
    var speedY: Float = 0f

    override fun reset() {
        gravity = 0f
        speedX = 0f
        speedY = 0f
    }
}
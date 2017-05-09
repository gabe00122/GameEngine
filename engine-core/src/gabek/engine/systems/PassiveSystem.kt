package gabek.engine.systems

import com.artemis.BaseSystem

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class PassiveSystem : BaseSystem() {
    override fun processSystem() {}

    override fun checkProcessing(): Boolean {
        isEnabled = false
        return false
    }
}
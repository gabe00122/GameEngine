package gabek.sm2.components

import com.artemis.Component

/**
 * @author Gabriel Keith
 */

class HealthCom: Component() {
    var maximumHealth: Int = 0
    var healthPoints: Int = 0

    var healthCooldown: Float = 0f
}
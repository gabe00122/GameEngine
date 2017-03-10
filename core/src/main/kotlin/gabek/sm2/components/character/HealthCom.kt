package gabek.sm2.components.character

import com.artemis.Component

/**
 * @author Gabriel Keith
 */

class HealthCom : Component() {
    var maximumHealth: Float = 0f
    var healthPoints: Float = 0f

    var healthCooldown: Float = 0f
}
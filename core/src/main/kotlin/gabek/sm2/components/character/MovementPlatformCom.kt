package gabek.sm2.components.character

import com.artemis.PooledComponent
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class MovementPlatformCom : PooledComponent() {
    var platformIndex: Int = -1

    override fun reset() {
        platformIndex = -1
    }
}
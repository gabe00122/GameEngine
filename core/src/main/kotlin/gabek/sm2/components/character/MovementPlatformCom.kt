package gabek.sm2.components.character

import com.artemis.PooledComponent
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class MovementPlatformCom : PooledComponent() {
    var platform: RFixture? = null

    override fun reset() {
        platform = null
    }
}
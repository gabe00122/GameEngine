package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class CharacterControllerCom : Component() {
    @JvmField var moveUp: Boolean = false
    @JvmField var moveDown: Boolean = false
    @JvmField var moveLeft: Boolean = false
    @JvmField var moveRight: Boolean = false
}
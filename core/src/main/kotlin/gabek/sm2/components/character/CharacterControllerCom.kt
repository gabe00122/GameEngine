package gabek.sm2.components.character

import com.artemis.Component
import com.artemis.annotations.PooledWeaver

/**
 * @author Gabriel Keith
 */
class CharacterControllerCom : Component() {
    var moveUp: Boolean = false
    var moveDown: Boolean = false
    var moveLeft: Boolean = false
    var moveRight: Boolean = false

    var jump: Boolean = false

    var primary: Boolean = false
}
package gabek.sm2.components.pellet

import com.artemis.Component

/**
 * @author Gabriel Keith
 */
class PelletCollisionCom : Component() {
    var damage: Float = 0f
    var kickback: Float = 0f

    var diesOnCollision: Boolean = false
    var diesOnAttack: Boolean = false
}
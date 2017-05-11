package gabek.engine.core.components.pellet

import gabek.engine.core.components.RComponent

/**
 * @author Gabriel Keith
 */
class PelletCollisionCom : RComponent<PelletCollisionCom>() {
    var damage: Float = 0f
    var kickback: Float = 0f

    var diesOnCollision: Boolean = false
    var diesOnAttack: Boolean = false

    override fun set(other: PelletCollisionCom) {
        damage = other.damage
        kickback = other.kickback

        diesOnCollision = other.diesOnCollision
        diesOnAttack = other.diesOnAttack
    }

    override fun reset() {
        damage = 0f
        kickback = 0f

        diesOnCollision = false
        diesOnAttack = false
    }

    override fun toString() = "PelletCollisionCom: damage = $damage, kickback = $kickback, diesOnCollision = $diesOnCollision, diesOnAttack = $diesOnAttack"
}
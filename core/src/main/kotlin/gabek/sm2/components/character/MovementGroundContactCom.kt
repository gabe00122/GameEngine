package gabek.sm2.components.character

import com.artemis.PooledComponent
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class MovementGroundContactCom : PooledComponent() {
    val contacts = mutableListOf<GroundContact>()
    val onGround get() = contacts.size > 0

    override fun reset() {
        contacts.clear()
    }

    fun indexOf(fixture: RFixture): Int =
            (0 until contacts.size).firstOrNull { contacts[it].fixture === fixture } ?: -1

    fun add(x: Float, y: Float, fixture: RFixture) {
        contacts.add(GroundContact(x, y, fixture))
    }

    fun remove(index: Int) {
        if (index == contacts.size - 1) {
            contacts.removeAt(index)
        } else {
            contacts[index] = contacts.removeAt(contacts.size - 1)
        }
    }

    fun remove(fixture: RFixture) {
        val index = indexOf(fixture)
        if (index > -1) {
            remove(index)
        }
    }

    data class GroundContact(var x: Float, var y: Float, val fixture: RFixture)
}
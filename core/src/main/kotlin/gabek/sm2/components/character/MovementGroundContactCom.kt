package gabek.sm2.components.character

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2
import gabek.sm2.physics.RFixture

/**
 * @author Gabriel Keith
 */
class MovementGroundContactCom : PooledComponent() {
    val contacts = mutableListOf<GroundContact>()
    val onGround get() = contacts.size > 0
    var platformIndex: Int = -1
    var platformMinAngle = 180f
    var platformMaxAngle = 360f

    override fun reset() {
        contacts.clear()
        platformIndex = -1
        platformMinAngle = 180f
        platformMaxAngle = 360f
    }

    fun indexOf(fixture: RFixture): Int =
            (0 until contacts.size).firstOrNull { contacts[it].fixture === fixture } ?: -1

    fun get(fixture: RFixture): GroundContact? {
        return contacts.firstOrNull { it.fixture === fixture }
    }

    fun getOrCreate(fixture: RFixture): GroundContact{
        return get(fixture) ?: add(fixture)
    }

    fun add(fixture: RFixture): GroundContact {
        val contact = GroundContact(fixture)
        contacts.add(contact)
        return contact
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

    class GroundContact(val fixture: RFixture){
        var numberOfPoints: Int = 0
        val points = Array(2, { Vector2() })
        val normal = Vector2()
    }
}
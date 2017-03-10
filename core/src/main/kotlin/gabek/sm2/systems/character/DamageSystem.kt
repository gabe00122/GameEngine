package gabek.sm2.systems.character

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import gabek.sm2.components.character.HealthCom
import gabek.sm2.systems.ParentSystem

/**
 * @author Gabriel Keith
 */
class DamageSystem : BaseSystem() {
    private lateinit var parentSystem: ParentSystem
    private lateinit var healthMapper: ComponentMapper<HealthCom>

    override fun processSystem() {}

    fun damage(entity: Int, amount: Float): Boolean {
        val health = parentSystem.recursiveGet(healthMapper, entity)
        if (health != null) {
            health.healthPoints -= amount
            return true
        } else {
            return false
        }
    }
}
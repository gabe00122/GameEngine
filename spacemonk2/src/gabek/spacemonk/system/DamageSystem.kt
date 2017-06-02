package gabek.engine.core.systems.character

import com.artemis.ComponentMapper
import gabek.engine.core.components.character.HealthCom
import gabek.engine.core.systems.PassiveSystem
import gabek.engine.core.systems.common.ParentSystem

/**
 * @author Gabriel Keith
 */
class DamageSystem : PassiveSystem() {
    private val deathListeners = ArrayList<DeathListener>()
    private lateinit var parentSystem: ParentSystem
    private lateinit var healthMapper: ComponentMapper<HealthCom>

    override fun processSystem() {}

    fun hasHealth(entity: Int): Boolean = healthMapper.has(entity)

    fun damage(entity: Int, amount: Float) {
        val health = healthMapper[entity]
        health.value -= amount

        if (health.value <= 0f) {
            deathListeners.forEach { it(entity, 0f, 0) }
        }
    }

    fun kill(entity: Int) {
        val health = healthMapper[entity]
        health.value = 0f

        deathListeners.forEach { it(entity, 0f, 0) }
    }

    fun addDeathListener(deathListener: DeathListener) {
        deathListeners.add(deathListener)
    }
}

typealias DeathListener = (entity: Int, damage: Float, damageType: Int) -> Unit
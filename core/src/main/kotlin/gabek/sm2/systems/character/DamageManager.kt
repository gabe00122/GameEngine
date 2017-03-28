package gabek.sm2.systems.character

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.utils.IntMap
import gabek.sm2.components.character.HealthCom
import gabek.sm2.systems.ParentSystem

/**
 * @author Gabriel Keith
 */
class DamageManager : BaseSystem() {
    private val deathListeners = ArrayList<DeathListener>()
    private lateinit var parentSystem: ParentSystem
    private lateinit var healthMapper: ComponentMapper<HealthCom>

    override fun processSystem() {}

    fun hasHealth(entity: Int): Boolean = healthMapper.has(entity)

    fun damage(entity: Int, amount: Float) {
        val health = healthMapper[entity]
        health.healthPoints -= amount

        if(health.healthPoints <= 0f){
            deathListeners.forEach { it(entity, 0f, 0) }
        }
    }

    fun kill(entity: Int) {
        val health = healthMapper[entity]
        health.healthPoints = 0f

        deathListeners.forEach { it(entity, 0f, 0) }
    }

    fun addDeathListener(deathListener: DeathListener){
        deathListeners.add(deathListener)
    }
}

typealias DeathListener = (entity: Int, damage: Float, damageType: Int) -> Unit
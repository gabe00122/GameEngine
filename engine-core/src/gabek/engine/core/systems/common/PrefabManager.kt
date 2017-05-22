package gabek.engine.core.systems.common

import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.prefab.Prefab
import gabek.engine.core.systems.PassiveSystem

/**
 * @author Gabriel Keith
 */
class PrefabManager(val kodein: Kodein, builder: Builder): PassiveSystem() {
    private val prefabMap = builder.factoryMap

    override fun processSystem() {}

    override fun initialize() {
        super.initialize()

        prefabMap.forEach { _, v ->
            v.initialise(kodein, world)
            v.define()
            v.assemble()
        }
    }

    fun getPrefab(name: String): Prefab {
        return prefabMap[name] ?: throw IllegalArgumentException("Factory not found with the name: $name")
    }

    fun create(name: String): Int = getPrefab(name).create()

    fun create(name: String, x: Float, y: Float): Int = getPrefab(name).create(x, y)

    override fun dispose() {
        for (factory in prefabMap.values) {
            factory.dispose()
        }
    }

    companion object {
        fun build(kodein: Kodein, builderFunction: (builder: Builder) -> Unit): PrefabManager {
            val builder = Builder()
            builderFunction(builder)
            return PrefabManager(kodein, builder)
        }
    }

    class Builder {
        internal val factoryMap = LinkedHashMap<String, Prefab>()

        fun bind(name: String, prefab: Prefab) {
            factoryMap.put(name, prefab)
            prefab.name = name
        }
    }
}
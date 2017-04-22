package gabek.sm2.systems.common

import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.prefab.Prefab
import gabek.sm2.systems.PassiveSystem

/**
 * @author Gabriel Keith
 */
class PrefabManager(val kodein: Kodein, builder: Builder) : PassiveSystem() {
    private val factoryMap = builder.factoryMap

    override fun processSystem() {}

    override fun initialize() {
        super.initialize()

        factoryMap.forEach { k, v ->
            v.initialise(kodein, world)
            v.define()
            v.assemble()
        }
    }

    fun getPrefab(name: String): Prefab {
        return factoryMap[name] ?: throw IllegalArgumentException("Factory not found with the name: $name")
    }

    fun create(name: String): Int = getPrefab(name).create()

    override fun dispose() {
        for (factory in factoryMap.values) {
            factory.dispose()
        }
    }

    companion object{
        fun build(kodein: Kodein, builderFunction: (builder: Builder) -> Unit): PrefabManager {
            val builder = Builder()
            builderFunction(builder)
            return PrefabManager(kodein, builder)
        }
    }

    class Builder{
        internal val factoryMap = LinkedHashMap<String, Prefab>()

        fun bind(name: String, prefab: Prefab){
            factoryMap.put(name, prefab)
        }
    }
}
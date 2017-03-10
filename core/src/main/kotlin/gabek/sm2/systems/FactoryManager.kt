package gabek.sm2.systems

import com.artemis.BaseSystem
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.factory.EntityFactory

/**
 * @author Gabriel Keith
 */
class FactoryManager(val kodein: Kodein, private val builderMap: List<Pair<String, EntityFactory.Builder>>) : BaseSystem() {
    private val factoryMap = mutableMapOf<String, EntityFactory>()

    override fun processSystem() {}

    override fun initialize() {
        super.initialize()

        for ((name, builder) in builderMap) {
            factoryMap.put(name, builder.build(kodein, world))
        }
    }

    fun getFactory(name: String): EntityFactory {
        return factoryMap[name] ?: throw IllegalArgumentException("Factory not found with the name: $name")
    }

    fun create(name: String): Int = getFactory(name).create()

    override fun dispose() {
        for (factory in factoryMap.values) {
            factory.dispose()
        }
    }
}
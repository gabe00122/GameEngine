package gabek.sm2.systems

import com.artemis.BaseSystem
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.factory.EntityFactory

/**
 * @author Gabriel Keith
 */
class FactoryManager(val kodein: Kodein, builder: Builder) : BaseSystem() {
    private val factoryMap = builder.factoryMap

    override fun processSystem() {}

    override fun initialize() {
        super.initialize()

        factoryMap.forEach { k, v ->
            v.initialize(kodein, world)
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

    companion object{
        fun build(kodein: Kodein, builderFunction: (builder: Builder) -> Unit): FactoryManager{
            val builder = Builder()
            builderFunction(builder)
            return FactoryManager(kodein, builder)
        }
    }

    class Builder{
        internal val factoryMap = LinkedHashMap<String, EntityFactory>()

        fun bind(name: String, entityFactory: EntityFactory){
            factoryMap.put(name, entityFactory)
        }
    }
}
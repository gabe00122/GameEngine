package gabek.sm2.systems

import com.artemis.BaseSystem
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.factory.EntityFactory

/**
 * @author Gabriel Keith
 */
class FactoryManager(val kodein: Kodein): BaseSystem(){
    private val factoryMap = mutableMapOf<String, EntityFactory>()

    override fun processSystem() {}

    fun setFactory(name: String, factory: EntityFactory){
        factoryMap.put(name, factory)
    }

    fun getFactory(name: String): EntityFactory{
        return factoryMap[name] ?: throw IllegalArgumentException("Factory not found with the name: $name")
    }

    override fun dispose() {
        for(factory in factoryMap.values){
            factory.dispose()
        }
    }
}
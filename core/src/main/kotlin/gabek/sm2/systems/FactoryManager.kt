package gabek.sm2.systems

import com.artemis.BaseSystem
import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.factory.EntityFactory

/**
 * @author Gabriel Keith
 */
class FactoryManager(val kodein: Kodein): BaseSystem(){
    private val factoryMap = mutableMapOf<Class<out EntityFactory>, EntityFactory>()

    override fun processSystem() {}

    inline fun <reified T: EntityFactory> getFactory() = getFactory(T::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <T: EntityFactory> getFactory(clazz: Class<T>): T = factoryMap.getOrPut(clazz) { create(clazz) } as T

    @Suppress("UNCHECKED_CAST")
    private fun <T: EntityFactory> create(clazz: Class<T>): T{
        clazz.constructors
                .filter {
                    it.parameters.size == 2 &&
                            it.parameters[0].type == Kodein::class.java &&
                            it.parameters[1].type == World::class.java
                }
                .forEach { return it.newInstance(kodein, world) as T }
        throw IllegalStateException()
    }

    override fun dispose() {
        for(factory in factoryMap.values){
            factory.dispose()
        }
    }
}
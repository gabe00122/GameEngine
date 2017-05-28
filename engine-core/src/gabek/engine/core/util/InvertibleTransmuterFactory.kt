package gabek.engine.core.util

import com.artemis.Component
import com.artemis.EntityTransmuterFactory
import com.artemis.World

/**
 * @another Gabriel Keith
 * @date 5/25/2017.
 */

class InvertibleTransmuterFactory(world: World) {
    private val transmuterBuilder = EntityTransmuterFactory(world)
    private val inverseTransmuterBuilder = EntityTransmuterFactory(world)

    fun <T: Component> add(comp: Class<T>): InvertibleTransmuterFactory{
        transmuterBuilder.add(comp)
        inverseTransmuterBuilder.remove(comp)
        return this
    }

    fun <T: Component> remove(comp: Class<T>): InvertibleTransmuterFactory{
        transmuterBuilder.remove(comp)
        inverseTransmuterBuilder.add(comp)
        return this
    }

    fun build() = InvertibleTransmuter(transmuterBuilder.build(), inverseTransmuterBuilder.build())
}
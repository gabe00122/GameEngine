package gabek.engine.core.util.symbol

import com.badlogic.gdx.utils.ObjectMap
import gabek.engine.core.util.createIfMissing

/**
 * @author Gabriel Keith
 * @date 7/29/2017
 */
class SymbolManager {
    private val contexts = ObjectMap<Class<*>, SymbolContext>()

    @Suppress("UNCHECKED_CAST")
    operator fun <I: Symbol> get(clazz: Class<I>, name: String): I {
        val context = contexts.createIfMissing(clazz, { SymbolContext(clazz) })
        return context.get(name) as I
    }

    inline operator fun <reified I: Symbol> get(name: String): I{
        return get(I::class.java, name)
    }
}
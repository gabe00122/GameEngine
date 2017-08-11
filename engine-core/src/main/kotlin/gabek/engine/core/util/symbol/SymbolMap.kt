package gabek.engine.core.util.symbol

import java.util.ArrayList
import kotlin.reflect.KClass

/**
 * @author Gabriel Keith
 * @date 7/29/2017
 */
class SymbolMap<S: Symbol, T>(
        val symbolClass: KClass<S>,
        private val defaultValue: () -> T
){
    private val data = ArrayList<T>()

    operator fun get(symbol: S): T{
        if(data.size <= symbol.id){
            grow(symbol.id + 1)
        }

        return data[symbol.id]
    }

    operator fun set(symbol: S, value: T){
        if(data.size <= symbol.id){
            grow(symbol.id + 1)
        }

        data[symbol.id] = value
    }

    private fun grow(size: Int){
        data.ensureCapacity(size)
        while(data.size < size){
            data.add(defaultValue())
        }
    }

    companion object {
        inline fun <reified S: Symbol, T> mapOf(noinline defaultValue: () -> T): SymbolMap<S, T>{
            return SymbolMap(S::class, defaultValue)
        }
    }
}

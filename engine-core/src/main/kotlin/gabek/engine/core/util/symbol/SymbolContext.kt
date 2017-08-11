package gabek.engine.core.util.symbol

import com.badlogic.gdx.utils.ObjectMap
import gabek.engine.core.util.createIfMissing

/**
 * @author Gabriel Keith
 * @date 7/29/2017
 */
internal class SymbolContext(private val identifierClazz: Class<*>){
    private var nextId = 0
    private val identifierMap = ObjectMap<String, Symbol>()

    fun get(name: String): Symbol {
        return identifierMap.createIfMissing(name) {
            val symbol: Symbol = identifierClazz.newInstance() as Symbol
            symbol.id = nextId++
            symbol.name = name

            symbol
        }
    }
}
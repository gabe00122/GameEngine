package gabek.engine.core.input

import gabek.engine.core.util.symbol.Symbol
import gabek.engine.core.util.symbol.SymbolManager

/**
 * @author Gabriel Keith
 * @date 7/29/2017
 */
class BindingSymbol : Symbol()

class CoreBindings(im: SymbolManager){
    private val className = this::class.java.name

    val moveUp: BindingSymbol = im[className + ":move_up"]
    val moveDown: BindingSymbol = im[className + ":move_down"]
    val moveLeft: BindingSymbol = im[className + ":move_left"]
    val moveRight: BindingSymbol = im[className + ":move_right"]
}
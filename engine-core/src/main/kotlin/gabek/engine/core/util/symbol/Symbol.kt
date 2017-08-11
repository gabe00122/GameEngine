package gabek.engine.core.util.symbol

/**
 * @author Gabriel Keith
 * @date 7/29/2017
 */
abstract class Symbol {
    var name: String = "undefined"
        internal set
    var id: Int = -1
        internal set

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return id
    }
}
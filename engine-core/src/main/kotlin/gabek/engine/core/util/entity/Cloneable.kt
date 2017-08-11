package gabek.engine.core.util.entity

/**
 * @author Gabriel Keith
 * @date 5/26/2017
 */

interface Cloneable<T: Cloneable<T>> {
    fun clone(): T
}
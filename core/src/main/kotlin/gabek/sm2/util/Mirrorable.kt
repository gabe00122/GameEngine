package gabek.sm2.util

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */

interface Mirrorable<T: Mirrorable<T>> {
    fun set(other: T)
}
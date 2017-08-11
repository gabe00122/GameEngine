package gabek.engine.core.util

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Pool

inline fun <reified T: Pool.Poolable> poolOf(crossinline newObj: ()->T) = object: Pool<T>() {
    override fun newObject() = newObj()
}


inline fun <K, V> ObjectMap<K, V>.createIfMissing(key: K, defaultValue: () -> V): V{
    var value = get(key)
    if(value != null){
        return value
    } else {
        value = defaultValue()
        put(key, value)
        return value
    }
}
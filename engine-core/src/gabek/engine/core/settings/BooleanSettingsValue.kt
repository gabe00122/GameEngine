package gabek.engine.core.settings

import com.badlogic.gdx.Preferences

/**
 * @author Gabriel Keith
 * @date 3/30/2017
 */
class BooleanSettingsValue(default: Boolean) : SettingsValue {
    private val listeners = ArrayList<(oldValue: Boolean, newValue: Boolean) -> Unit>()

    var value: Boolean = default
        set(value) {
            if (field != value) {
                listeners.forEach { it(field, value) }
                field = value
            }
        }

    fun onChange(listener: (oldValue: Boolean, newValue: Boolean) -> Unit) {
        listeners.add(listener)
    }

    override fun save(name: String, pref: Preferences) {
        pref.putBoolean(name, value)
    }

    override fun load(name: String, pref: Preferences) {
        value = pref.getBoolean(name, value)
    }

    interface ChangeListener {
        fun onChange(oldValue: Float, newValue: Float)
    }
}
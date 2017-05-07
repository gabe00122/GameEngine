package gabek.sm2.settings

import com.badlogic.gdx.Preferences

/**
 * @author Gabriel Keith
 * @date 3/30/2017
 */
class StringSettingsValue(default: String) : SettingsValue() {
    private val listeners = ArrayList<(oldValue: String, newValue: String) -> Unit>()

    var value: String = default
        set(value) {
            if (field != value) {
                listeners.forEach { it(field, value) }
                field = value
            }
        }

    fun onChange(listener: (oldValue: String, newValue: String) -> Unit) {
        listeners.add(listener)
    }

    override fun save(name: String, pref: Preferences) {
        pref.putString(name, value)
    }

    override fun load(name: String, pref: Preferences) {
        value = pref.getString(name, value)
    }

    interface ChangeListener {
        fun onChange(oldValue: Float, newValue: Float)
    }
}
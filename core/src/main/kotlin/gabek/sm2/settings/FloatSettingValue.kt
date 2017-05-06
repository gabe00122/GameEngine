package gabek.sm2.settings

import com.badlogic.gdx.Preferences

/**
 * @author Gabriel Keith
 * @date 3/15/2017
 */
class FloatSettingValue(default: Float) : SettingsValue() {
    private val listeners = ArrayList<(oldValue: Float, newValue: Float) -> Unit>()

    var value: Float = default
        set(value) {
            if (field != value) {
                listeners.forEach { it(field, value) }
                field = value
            }
        }

    fun onChange(listener: (oldValue: Float, newValue: Float) -> Unit) {
        listeners.add(listener)
    }

    override fun save(name: String, pref: Preferences) {
        pref.putFloat(name, value)
    }

    override fun load(name: String, pref: Preferences) {
        value = pref.getFloat(name, value)
    }

    interface ChangeListener {
        fun onChange(oldValue: Float, newValue: Float)
    }
}
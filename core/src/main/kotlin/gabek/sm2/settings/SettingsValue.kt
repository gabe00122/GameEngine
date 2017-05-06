package gabek.sm2.settings

import com.badlogic.gdx.Preferences

/**
 * @author Gabriel Keith
 * @date 3/15/2017
 */
abstract class SettingsValue {
    abstract fun save(name: String, pref: Preferences)
    abstract fun load(name: String, pref: Preferences)
}
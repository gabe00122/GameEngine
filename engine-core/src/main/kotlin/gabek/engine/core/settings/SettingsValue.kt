package gabek.engine.core.settings

import com.badlogic.gdx.Preferences

/**
 * @author Gabriel Keith
 * @date 3/15/2017
 */
interface SettingsValue {
    fun save(name: String, pref: Preferences)
    fun load(name: String, pref: Preferences)
}
package gabek.sm2.settings

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

/**
 * @author Gabriel Keith
 * @date 3/15/2017
 */
class Settings{
    private val preferences: Preferences

    private val values = LinkedHashMap<String, SettingsValue>()

    constructor(pref: String){
        preferences = Gdx.app.getPreferences(pref)
        initialize()
    }

    constructor(pref: Preferences){
        preferences = pref
        initialize()
    }

    private fun initialize(){
        values.put("fullscreen", BooleanSettingsValue(false))
        values.put("resolution", StringSettingsValue("800x800"))

        values.put("ui_scale", FloatSettingValue(2.0f))

        values.put("music_level", FloatSettingValue(1.0f))
        values.put("effect_level", FloatSettingValue(1.0f))
        load()
    }

    fun getFloat(name: String): FloatSettingValue{
        return values[name] as FloatSettingValue
    }

    fun getBoolean(name: String): BooleanSettingsValue {
        return values[name] as BooleanSettingsValue
    }

    fun getString(name: String): StringSettingsValue {
        return values[name] as StringSettingsValue
    }

    fun load(){
        for((name, value) in values){
            value.load(name, preferences)
        }
    }

    fun save(){
        for((name, value) in values){
            value.save(name, preferences)
        }
        preferences.flush()
    }
}
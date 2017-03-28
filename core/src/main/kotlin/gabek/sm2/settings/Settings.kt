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
        initilize()
    }

    constructor(pref: Preferences){
        preferences = pref
        initilize()
    }

    private fun initilize(){
        values.put("ui_scale", FloatSettingValue(2.0f))
        load()
    }

    fun getFloatValue(name: String): FloatSettingValue{
        return values[name] as FloatSettingValue
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
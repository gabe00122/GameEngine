
package gabek.sm2.audio

import com.badlogic.gdx.audio.Music
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.sm2.assets.Assets
import gabek.sm2.settings.Settings

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class MusicPlayer(val kodein: Kodein){
    val settings: Settings = kodein.instance()
    val assets: Assets = kodein.instance()

    val musicLevel = settings.getFloat("music_level")

    var currentSong: Music? = null

    init {
        musicLevel.onChange { oldValue, newValue ->
            currentSong?.volume = newValue
        }
    }

    fun playSong(name: String){
        currentSong?.stop()

        currentSong = assets.retrieveMusic(name)
        currentSong?.volume = musicLevel.value
        currentSong?.isLooping = true
        currentSong?.play()
    }

}
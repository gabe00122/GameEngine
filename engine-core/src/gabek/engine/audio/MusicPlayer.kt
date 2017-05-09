package gabek.engine.audio

import com.badlogic.gdx.audio.Music
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.assets.Assets
import gabek.engine.settings.Settings

/**
 * @author Gabriel Keith
 * @date 4/1/2017
 */
class MusicPlayer(val kodein: Kodein) {
    val settings: Settings = kodein.instance()
    val assets: Assets = kodein.instance()

    val musicLevel = settings.getFloat("music_level")

    var currentSong: Music? = null

    init {
        musicLevel.onChange { _, newValue ->
            val currentSong = currentSong
            if(currentSong != null) {
                if (newValue > 0f) {
                    currentSong.volume = newValue

                    if(!currentSong.isPlaying){
                        currentSong.play()
                    }
                } else if(currentSong.isPlaying){
                    currentSong.stop()
                }
            }
        }
    }

    fun playSong(name: String) {
        currentSong?.stop()

        currentSong = assets.fineMusic(name)
        currentSong?.volume = musicLevel.value
        currentSong?.isLooping = true
        currentSong?.play()
    }

}
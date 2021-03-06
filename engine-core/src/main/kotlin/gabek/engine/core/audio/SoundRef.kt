package gabek.engine.core.audio

import com.badlogic.gdx.audio.Sound

/**
 * @another Gabriel Keith
 * @date 5/23/2017.
 */

class SoundRef(val sound: Sound, val soundName: String) {

    internal fun play(volume: Float): Long {
        return sound.play()
    }
}
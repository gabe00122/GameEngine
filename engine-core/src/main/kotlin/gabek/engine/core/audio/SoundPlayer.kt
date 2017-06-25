package gabek.engine.core.audio



/**
 * @author Gabriel Keith
 * @date 5/31/2017
 */
class SoundPlayer(){
    var volume: Float = 1f


    fun play(sound: SoundRef, volume: Float){
        sound.play(this.volume * volume)
    }
}
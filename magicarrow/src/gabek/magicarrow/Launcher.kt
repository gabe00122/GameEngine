package gabek.magicarrow

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences
import gabek.engine.core.settings.Settings

/**
 * @another Gabriel Keith
 * @date 5/16/2017.
 */

fun main(args: Array<String>){
    val config = Lwjgl3ApplicationConfiguration()
    config.useOpenGL3(true, 3, 2)

    config.setWindowedMode(600, 600)
    config.useVsync(true)

    val settings = Settings(Lwjgl3Preferences("magicarrow.pref", "./"))

    Lwjgl3Application(MagicArrow(settings), config)
}
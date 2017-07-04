package gabek.onebreath

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
    //config.useOpenGL3(true, 3, 2)

    config.setTitle("OneBreath")

    config.setWindowedMode(800, 800)
    config.useVsync(true)

    //config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())

    Lwjgl3Application(OneBreath(), config)
}
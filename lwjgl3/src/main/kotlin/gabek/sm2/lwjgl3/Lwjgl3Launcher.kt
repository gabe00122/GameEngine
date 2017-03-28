package gabek.sm2.lwjgl3

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences
import gabek.sm2.Core
import gabek.sm2.settings.Settings
import java.io.File

/**
 * @author Gabriel Keith
 */

fun main(args: Array<String>){
    //val config = LwjglApplicationConfiguration()
    //config.width = 800
    //config.height = 800

    //LwjglApplication(Core(), config)
    val settings = Settings(Lwjgl3Preferences(Lwjgl3FileHandle("./settings.pref", Files.FileType.External)))
    Lwjgl3Application(Core(settings), getDefaultConfig(settings))
}


fun getDefaultConfig(settings: Settings): Lwjgl3ApplicationConfiguration{
    val config = Lwjgl3ApplicationConfiguration()
    config.useOpenGL3(true, 3, 2)
    config.enableGLDebugOutput(true, System.err)

    config.setTitle("SpaceMonk2")
    config.setWindowedMode(600, 600)
    //config.useVsync(false)
    //config.setIdleFPS()

    //config.setBackBufferConfig(8, 8, 8, 8, 0, 0, 0)
    //config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())

    return config
}
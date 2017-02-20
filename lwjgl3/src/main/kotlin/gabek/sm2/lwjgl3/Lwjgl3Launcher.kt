package gabek.sm2.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import gabek.sm2.Core

/**
 * @author Gabriel Keith
 */

fun main(args: Array<String>){
    //val config = LwjglApplicationConfiguration()
    //config.width = 800
    //config.height = 800

    //LwjglApplication(Core(), config)
    Lwjgl3Application(Core(), getDefaultConfig())
}


fun getDefaultConfig(): Lwjgl3ApplicationConfiguration{
    val config = Lwjgl3ApplicationConfiguration()
    config.useOpenGL3(true, 3, 2)

    config.setTitle("SpaceMonk2")
    config.setWindowedMode(800, 800)
    //config.useVsync(false)
    //config.setIdleFPS()

    config.setBackBufferConfig(8, 8, 8, 8, 0, 0, 0)
    //config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode())

    return config
}
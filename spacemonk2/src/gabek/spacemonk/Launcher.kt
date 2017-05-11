package gabek.spacemonk

import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.backends.lwjgl3.*
import gabek.engine.core.settings.Settings

/**
 * @author Gabriel Keith
 */

fun main(args: Array<String>){
    val settings = Settings(Lwjgl3Preferences(Lwjgl3FileHandle("spacemonk2.pref", Files.FileType.External)))
    Lwjgl3Application(Core(settings), getDefaultConfig(settings))
}

fun getDisplayMode(name: String): Graphics.DisplayMode{
    val split = name.split("x")
    val width = split[0].toInt()
    val height = split[1].toInt()

    return Lwjgl3ApplicationConfiguration.getDisplayModes()
            .filter { it.refreshRate == 60 }
            .firstOrNull { it.width == width && it.height == height }
            ?: Gdx.graphics.displayModes[0]
}

fun getDefaultConfig(settings: Settings): Lwjgl3ApplicationConfiguration{
    val config = Lwjgl3ApplicationConfiguration()
    config.useOpenGL3(true, 3, 2)
    config.enableGLDebugOutput(true, System.err)

    config.setTitle("SpaceMonk")

    if(settings.getBoolean("fullscreen").value) {
        config.setFullscreenMode(getDisplayMode(settings.getString("resolution").value))
    } else {
        config.setWindowedMode(600, 600)
    }

    return config
}
package gabek.spacemonk

import com.artemis.World
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.physics.box2d.Box2D
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.kotcrab.vis.ui.VisUI
import gabek.engine.core.assets.Assets
import gabek.engine.core.audio.MusicPlayer
import gabek.engine.core.console.Console
import gabek.engine.core.input.InputManager
import gabek.engine.core.screen.ScreenManager
import gabek.spacemonk.screen.buildScreenManager
import gabek.engine.core.settings.Settings
import gabek.engine.core.world.*

/**
 * @author Gabriel Keith
 */

class Core(val settings: Settings) : ApplicationAdapter() {
    lateinit var kodein: Kodein
    lateinit var screenManager: ScreenManager

    override fun create() {
        ShaderProgram.prependFragmentCode = "#version 150\n"
        ShaderProgram.prependVertexCode = "#version 150\n"

        VisUI.load("assets/ui/skin.json")
        Box2D.init()

        kodein = Kodein {
            bind<ScreenManager>() with singleton { buildScreenManager(this) }
            bind<Settings>() with singleton { settings }
            bind<Assets>() with singleton { Assets("assets/manifest.json") }
            bind<MusicPlayer>() with singleton { MusicPlayer(kodein) }
            bind<InputManager>() with singleton { buildInputManager() }
            bind<World>() with singleton { buildWorld(this) }
            bind<WorldConfig>() with singleton { WorldConfig() }

            bind<Console>() with singleton { Console(kodein) }
        }
        Gdx.app.applicationLogger = kodein.instance<Console>()

        kodein.instance<Assets>().finish()
        kodein.instance<MusicPlayer>().playSong("mixdown")

        screenManager = kodein.instance()
        Gdx.input.inputProcessor = InputMultiplexer(screenManager.inputProcessor, kodein.instance<InputManager>().inputProcessor)
        //Gdx.input.inputProcessor = screenManager.inputProcessor

        quickLaunch()
    }


    fun quickLaunch() {
        val screenManager: ScreenManager = kodein.instance()
        val worldConfig: WorldConfig = kodein.instance()
        val inputManager: InputManager = kodein.instance()

        worldConfig.players.add(PlayerInfo(0, inputManager.getPlayerInput(0)))

        screenManager.show("playing")
    }

    override fun dispose() {
        screenManager.dispose()
        settings.save()
    }

    override fun render() {
        screenManager.update(Gdx.graphics.deltaTime)

        Gdx.gl.glClearColor(34f / 255f, 32f / 255f, 52f / 255f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        screenManager.render()
    }

    override fun resize(width: Int, height: Int) {
        screenManager.resize(width, height)
    }
}
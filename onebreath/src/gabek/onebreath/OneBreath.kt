package gabek.onebreath

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
import gabek.engine.core.settings.Settings

/**
* @another Gabriel Keith
* @date 5/16/2017.
*/

class OneBreath(val settings: Settings): ApplicationAdapter() {
    lateinit var kodein: Kodein
    lateinit var screenManager: ScreenManager

    override fun create() {
        Box2D.init()
        VisUI.load("assets/ui/skin.json")

        ShaderProgram.prependFragmentCode = "#version 150\n"
        ShaderProgram.prependVertexCode = "#version 150\n"

        kodein = Kodein {
            bind<ScreenManager>() with singleton { buildScreenManager(kodein) }
            bind<Assets>() with singleton { Assets() }
            bind<Settings>() with singleton { settings }
            bind<MusicPlayer>() with singleton { MusicPlayer(this) }
            bind<InputManager>() with singleton { buildInputManager() }
            bind<World>() with singleton { buildWorld(this) }

            bind<Console>() with singleton { Console(this) }
        }
        kodein.instance<Assets>().finish()
        screenManager = kodein.instance()

        Gdx.input.inputProcessor = InputMultiplexer(
                screenManager.inputProcessor,
                kodein.instance<InputManager>().inputProcessor
        )
    }

    override fun resize(width: Int, height: Int) {
        screenManager.resize(width, height)
    }

    override fun render() {
        //Gdx.gl.glClearColor(69/255f, 40/255f, 60/255f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)

        screenManager.update(Gdx.graphics.deltaTime)
        screenManager.render()
    }

    override fun dispose() {
        kodein.instance<Assets>().dispose()
        kodein.instance<Settings>().save()
        kodein.instance<ScreenManager>().dispose()
        kodein.instance<World>().dispose()
    }
}
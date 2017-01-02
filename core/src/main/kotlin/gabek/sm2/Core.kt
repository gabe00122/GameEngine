package gabek.sm2

import com.artemis.World
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.physics.box2d.Box2D
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.kotcrab.vis.ui.VisUI
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.screens.ScreenManager
import gabek.sm2.screens.buildScreenManager
import gabek.sm2.world.buildWorld

/**
 * @author Gabriel Keith
 */

class Core : ApplicationAdapter() {
    lateinit var screenManager: ScreenManager

    override fun create() {
        VisUI.load("ui/skin.json")
        Box2D.init()

        val kodein = Kodein {
            bind<ScreenManager>() with singleton { buildScreenManager(this) }
            bind<Assets>() with singleton { Assets() }
            bind<PlayerInputManager>() with singleton { PlayerInputManager(this) }
            bind<World>() with singleton { buildWorld(this) }
        }
        kodein.instance<Assets>().finish()

        screenManager = kodein.instance()
        Gdx.input.inputProcessor = InputMultiplexer(kodein.instance<PlayerInputManager>().inputProcessor, screenManager.inputProcessor)
    }

    override fun dispose() {
        screenManager.dispose()
    }

    override fun render() {
        screenManager.update(Gdx.graphics.deltaTime)

        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT)
        screenManager.render()
    }

    override fun resize(width: Int, height: Int) {
        screenManager.resize(width, height)
    }
}
package gabek.engine.core

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.VisUI
import gabek.engine.core.assets.Assets
import gabek.engine.core.input.InputManager
import gabek.engine.core.screen.Screen
import gabek.engine.core.screen.ScreenManager

/**
 * @author Gabriel Keith
 * @date 7/17/2017
 */

abstract class RGame: ApplicationListener {
    val skinName: String = "assets/ui/skin.json"

    lateinit var kodein: Kodein
    var screenManager: ScreenManager? = null


    var assets: Assets? = null
    var tempStage: Stage? = null


    override fun create() {
        Box2D.init()
        VisUI.load(skinName)

        setup()
        kodein = Kodein(init = this::kodeinSetup)

        assets = kodein.instance()

        tempStage = Stage(ScreenViewport(), kodein.instance())

        val splash: Screen = kodein.instance("splash")
        tempStage!!.addActor(splash.root)
    }

    abstract fun kodeinSetup(builder: Kodein.Builder)
    abstract fun setup()

    open fun finishedLoading() {
        screenManager = kodein.instance()
        setupInput()
    }

    override fun dispose() {}

    override fun resize(width: Int, height: Int) {
        screenManager?.resize(width, height)
    }

    override fun render() {
        val delta = Gdx.graphics.deltaTime
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)

        if(!assets!!.update()){
            tempStage!!.act(delta)
            tempStage!!.draw()
        } else {
            if(screenManager == null) {
                finishedLoading()
            }
            screenManager!!.update(delta)
            screenManager!!.render()
        }
    }

    private fun setupInput(){
        Gdx.input.inputProcessor = InputMultiplexer(
                screenManager!!.inputProcessor,
                kodein.instance<InputManager>().inputProcessor
        )
    }

    override fun pause() {}

    override fun resume() {}
}
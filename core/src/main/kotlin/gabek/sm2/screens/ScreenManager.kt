package gabek.sm2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisLabel
import gabek.sm2.settings.Settings

/**
 * @author Gabriel Keith
 */
class ScreenManager(val kodein: Kodein) : Disposable {
    val batch: SpriteBatch = SpriteBatch()
    val stage: Stage
    val inputProcessor: InputProcessor get() = stage

    private val screenBuilderMap = mutableMapOf<String, () -> Screen>()
    var currentScreen: Screen? = null
        private set

    private val fpsContainer: Container<VisLabel>
    private val fpsLabel: VisLabel = VisLabel("", Color.RED)

    init {
        val uiScale = kodein.instance<Settings>().getFloat("ui_scale")

        val viewport = ScreenViewport()
        viewport.unitsPerPixel = 1f / uiScale.value
        uiScale.onChange { _, newValue ->
            viewport.unitsPerPixel = 1f / newValue
            viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        }

        stage = Stage(viewport, batch)

        //fps tracking

        fpsContainer = Container(fpsLabel)
        fpsContainer.setFillParent(true)
        fpsContainer.align(Align.topRight)

        stage.root.addActor(fpsContainer)
    }

    fun update(delta: Float) {
        fpsLabel.setText("FPS: ${Gdx.graphics.framesPerSecond}")

        currentScreen?.update(delta)
        stage.act(delta)
    }

    fun render() {
        currentScreen?.render(batch)
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        currentScreen?.resize(width, height)
        stage.viewport.update(width, height, true)
    }

    fun bind(name: String, screen: () -> Screen) {
        screenBuilderMap[name] = screen
    }

    fun show(screenName: String) {
        val screenBuilder = screenBuilderMap[screenName]
        if (screenBuilder != null) {
            val screen = screenBuilder()
            screen.manager = this

            currentScreen?.hide()
            currentScreen?.dispose()
            stage.root.removeActor(currentScreen?.root)

            screen.show()
            stage.root.addActorAt(0, screen.root)
            currentScreen = screen
        } else {
            error("screen $screenName dose not exist")
        }
    }

    override fun dispose() {
        batch.dispose()
    }
}
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
import com.kotcrab.vis.ui.widget.VisLabel

/**
 * @author Gabriel Keith
 */
class ScreenManager(val kodein: Kodein) : Disposable {
    val batch: SpriteBatch
    val stage: Stage
    val inputProcessor: InputProcessor get() = stage

    private val screenBuilderMap: MutableMap<String, () -> Screen>
    private var currentScreen: Screen? = null

    private val fpsContainer: Container<VisLabel>
    private val fpsLabel: VisLabel

    init {
        batch = SpriteBatch()
        val viewport = ScreenViewport()
        viewport.unitsPerPixel = 0.5f

        stage = Stage(viewport, batch)
        screenBuilderMap = mutableMapOf()

        //fps tracking
        fpsLabel = VisLabel("", Color.RED)

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
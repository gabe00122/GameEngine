package gabek.engine.core.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.ConsoleGuiOverlay
import gabek.engine.core.graphics.Profiler
import gabek.engine.core.graphics.ProfilerOverlay
import gabek.engine.core.settings.Settings

/**
 * @author Gabriel Keith
 */
class ScreenManager(val kodein: Kodein) : Disposable {
    val batch: Batch = kodein.instance()
    val stage: Stage
    val inputProcessor: InputProcessor get() = stage

    private val screenBuilderMap = HashMap<String, () -> Screen>()
    var currentScreen: Screen? = null
        private set

    var overlay: Screen? = null
        set(value) {
            val oldValue = field
            field = value

            if(value != null){
                stage.root.addActor(value.root)
            } else if(oldValue != null){
                stage.root.removeActor(oldValue.root)
            }
        }

    private val profiler = Profiler()
    private val profilerOverlay = ProfilerOverlay(profiler)

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

        overlay = ConsoleGuiOverlay(kodein)

        stage.root.addActor(profilerOverlay)
    }

    fun update(delta: Float) {
        profiler.update(delta)

        currentScreen?.update(delta)
        overlay?.update(delta)
        stage.act(delta)
    }

    fun render() {
        currentScreen?.render(batch)
        overlay?.render(batch)
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        currentScreen?.resize(width, height)
        overlay?.resize(width, height)
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
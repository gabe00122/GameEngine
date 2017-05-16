package gabek.spacemonk.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisSelectBox
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.engine.core.screen.Screen
import gabek.engine.core.settings.Settings
import ktx.actors.onChange
import ktx.collections.toGdxArray

/**
 * @author Gabriel Keith
 * @date 3/15/2017
 */
class GraphicsSettingsMenu(kodein: Kodein) : Screen() {
    init {
        val setting: Settings = kodein.instance()
        val resolution = setting.getString("resolution")
        val fullscreen = setting.getBoolean("fullscreen")

        val uiScale = setting.getFloat("ui_scale")

        val displayModes = Gdx.graphics.displayModes

        val resolutionChoiceBox = VisSelectBox<String>()
        resolutionChoiceBox.items = displayList
        resolutionChoiceBox.selected = resolution.value
        resolutionChoiceBox.onChange { _, _ ->
            resolution.value = resolutionChoiceBox.selected
            if (fullscreen.value) {
                Gdx.graphics.setFullscreenMode(getDisplayMode(resolution.value))
            }
        }

        val fullscreenCheckBox = VisCheckBox("")
        fullscreenCheckBox.isChecked = fullscreen.value
        fullscreenCheckBox.onChange { _, _ ->
            fullscreen.value = fullscreenCheckBox.isChecked
            if (fullscreen.value) {
                Gdx.graphics.setFullscreenMode(getDisplayMode(resolution.value))
                Gdx.graphics.setVSync(true)
            } else {
                Gdx.graphics.setWindowedMode(600, 600)
                Gdx.graphics.setVSync(true)
            }
        }

        val uiScaleChoiceBox = VisSelectBox<UiScale>()
        uiScaleChoiceBox.setItems(UiScale.SCALE1, UiScale.SCALE2, UiScale.SCALE3, UiScale.SCALE4)
        uiScaleChoiceBox.selected = closestValue(uiScale.value)

        uiScaleChoiceBox.onChange { _, _ ->
            uiScale.value = uiScaleChoiceBox.selected.factor
        }


        val back = VisTextButton("Back")
        back.onChange { _, _ ->
            manager.show("settings")
        }


        val window = VisWindow("Graphics")
        window.isMovable = false
        val whole = 5f
        val half = 2.5f
        with(window) {
            add("Resolution: ").pad(whole, whole, half, half)
            add(resolutionChoiceBox).pad(whole, half, half, whole).align(Align.left)
            row()

            add("Fullscreen: ").pad(half, whole, half, half)
            add(fullscreenCheckBox).pad(half, half, half, whole).align(Align.left)
            row()

            add("Ui Scale: ").pad(half, whole, half, half)
            add(uiScaleChoiceBox).pad(half, half, half, whole).align(Align.left)
            row()

            add(back).pad(half, whole, whole, whole).colspan(2)
        }


        val container = Container(window)
        container.setFillParent(true)
        root.addActor(container)
    }

    private fun closestValue(factor: Float): UiScale {
        return UiScale.values().firstOrNull { factor == it.factor } ?: UiScale.SCALE1
    }

    private fun getDisplayMode(name: String): Graphics.DisplayMode {
        val split = name.split("x")
        val width = split[0].toInt()
        val height = split[1].toInt()

        return Gdx.graphics.displayModes
                .filter { it.refreshRate == 60 }
                .firstOrNull { it.width == width && it.height == height }
                ?: Gdx.graphics.displayModes[0]
    }

    private val displayList: com.badlogic.gdx.utils.Array<String> get() {
        return Gdx.graphics.displayModes
                .filter { it.refreshRate == 60 }
                .map { "${it.width}x${it.height}" }
                .toGdxArray()
    }

    enum class UiScale(val text: String, val factor: Float) {
        SCALE1("Size 1", 1f), SCALE2("Size 2", 2f), SCALE3("Size 3", 3f), SCALE4("Size 4", 4f);

        override fun toString(): String {
            return text
        }
    }
}
package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisSlider
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.settings.Settings
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 * @date 3/30/2017
 */

class SoundSettingsMenu(val kodein: Kodein) : Screen() {
    init {
        val settings: Settings = kodein.instance()
        val musicLevel = settings.getFloat("music_level")
        val effectLevel = settings.getFloat("effect_level")

        val musicLevelSlider = VisSlider(0f, 100f, 5f, false)
        val musicLevelMonitor = VisLabel("")
        musicLevelMonitor.setAlignment(Align.center)
        musicLevelSlider.onChange { _, _ ->
            musicLevelMonitor.setText(musicLevelSlider.value.toInt().toString())
            musicLevel.value = musicLevelSlider.value / 100f
        }
        //musicLevelRow.add("Music Level: ")

        val effectLevelSlider = VisSlider(0f, 100f, 5f, false)
        val effectLevelMonitor = VisLabel("")
        effectLevelMonitor.setAlignment(Align.center)
        effectLevelSlider.onChange { _, _ ->
            effectLevelMonitor.setText(effectLevelSlider.value.toInt().toString())
            effectLevel.value = effectLevelSlider.value / 100f
        }

        musicLevelSlider.value = musicLevel.value * 100f
        effectLevelSlider.value = effectLevel.value * 100f

        //effectLevelRow.add("Sound Effect Level: ")

        val backBut = VisTextButton("Back")
        backBut.onChange { _, _ ->
            manager.show("settings")
        }

        val window = VisWindow("Sound")
        window.isMovable = false

        val whole = 5f
        val half = 2.5f
        with(window) {
            add("Music Level: ").pad(whole, whole, half, half)
            add(musicLevelSlider).pad(whole, half, half, half)
            add(musicLevelMonitor).pad(whole, half, whole, half).prefWidth(20f).row()

            add("Sound Effect Level: ").pad(half, whole, half, half)
            add(effectLevelSlider).pad(half, half, half, half)
            add(effectLevelMonitor).pad(half, half, whole, half).prefWidth(20f).row()

            add(backBut).pad(half, whole, whole, whole).colspan(3)
        }

        val container = Container(window)
        container.setFillParent(true)
        root.addActor(container)
    }
}
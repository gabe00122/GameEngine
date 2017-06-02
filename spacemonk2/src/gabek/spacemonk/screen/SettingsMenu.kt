package gabek.spacemonk.screen

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.engine.core.input.InputManager
import gabek.engine.core.screen.Screen
import gabek.spacemonk.ui.MenuControl
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class SettingsMenu(kodein: Kodein) : Screen() {
    init {
        val graphics = VisTextButton("Graphics")
        graphics.onChange { _, _ ->
            manager.show("settings_graphics")
        }

        val sound = VisTextButton("Sound")
        sound.onChange { _, _ ->
            manager.show("settings_sound")
        }

        val controller = VisTextButton("Controllers")
        controller.onChange { _, _ ->
            manager.show("settings_controller")
        }

        val backBut = VisTextButton("Back")
        backBut.onChange { _, _ ->
            manager.show("main")
        }

        val menuControl = MenuControl(graphics, sound, controller, backBut)
        menuControl.playerInput = kodein.instance<InputManager>()

        val window = VisWindow("Settings")
        window.isMovable = false
        window.add(menuControl)

        val con = Container(window)
        con.setFillParent(true)
        root.addActor(con)
    }
}
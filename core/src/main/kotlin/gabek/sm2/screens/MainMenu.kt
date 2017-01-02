package gabek.sm2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.ui.MenuControl
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class MainMenu(val kodein: Kodein) : Screen() {
    init {
        val window = VisWindow("Main Menu", "default")
        window.isMovable = false
        window.isResizable = false

        val startBut = VisTextButton("Start", "toggle")
        startBut.onChange { inputEvent, visTextButton ->
            manager.show("playing")
        }

        val settingsBut = VisTextButton("Settings", "toggle")
        settingsBut.onChange { changeEvent, visTextButton ->
            manager.show("settings")
        }

        val quitBut = VisTextButton("Quit", "toggle")
        quitBut.onChange { inputEvent, visTextButton ->
            Gdx.app.exit()
        }
        val menu = MenuControl(startBut, settingsBut, quitBut)
        menu.playerInput = kodein.instance<PlayerInputManager>()

        window.add(menu).fill()
        val con = Container(window)
        con.setFillParent(true)
        root.addActor(con)
    }
}
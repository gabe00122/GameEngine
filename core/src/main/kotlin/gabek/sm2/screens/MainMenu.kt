package gabek.sm2.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.screens.Screen
import gabek.sm2.ui.MenuControl
import gabek.sm2.input.PlayerInput
import gabek.sm2.input.PlayerInputManager
import ktx.actors.alpha
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.vis.table

/**
 * @author Gabriel Keith
 */
class MainMenu(val kodein: Kodein) : Screen() {
    init{
        val window = VisWindow("Main Menu", "default")
        window.isMovable = false
        window.isResizable = false

        val startBut = VisTextButton("Start", "toggle")
        startBut.onChange{ inputEvent, visTextButton ->
            manager.show("playing")
        }

        val settingsBut = VisTextButton("Settings", "toggle")
        settingsBut.onChange{ changeEvent, visTextButton ->
            manager.show("settings")
        }

        val quitBut = VisTextButton("Quit", "toggle")
        quitBut.onChange{ inputEvent, visTextButton ->
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
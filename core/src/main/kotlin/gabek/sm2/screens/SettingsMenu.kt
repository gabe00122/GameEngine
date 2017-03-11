package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTextButton
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.ui.MenuControl
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class SettingsMenu(kodein: Kodein) : Screen() {
    init {
        val backBut = VisTextButton("Back")
        backBut.onChange { _, _ ->
            manager.show("main")
        }

        val menuControl = MenuControl(backBut)
        menuControl.playerInput = kodein.instance<PlayerInputManager>()

        val con = Container<MenuControl>(menuControl)
        con.setFillParent(true)
        root.addActor(con)
    }
}
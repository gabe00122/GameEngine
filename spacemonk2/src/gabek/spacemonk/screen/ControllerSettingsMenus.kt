package gabek.spacemonk.screen

import gabek.engine.core.screen.Screen
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 * @date 3/30/2017
 */
class ControllerSettingsMenus(kodein: com.github.salomonbrys.kodein.Kodein) : Screen() {
    init {
        val backBut = com.kotcrab.vis.ui.widget.VisTextButton("Back")
        backBut.onChange { _, _ ->
            manager.show("settings")
        }


        val window = com.kotcrab.vis.ui.widget.VisWindow("WIP")
        window.isMovable = false
        window.add(backBut).pad(5f)

        val container = com.badlogic.gdx.scenes.scene2d.ui.Container(window)
        container.setFillParent(true)
        root.addActor(container)
    }
}
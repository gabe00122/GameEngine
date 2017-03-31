package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 * @date 3/30/2017
 */
class ControllerSettingsMenus(kodein: Kodein): Screen(){
    init {
        val backBut = VisTextButton("Back")
        backBut.onChange { _, _ ->
            manager.show("settings")
        }


        val window = VisWindow("WIP")
        window.isMovable = false
        window.add(backBut).pad(5f)

        val container = Container(window)
        container.setFillParent(true)
        root.addActor(container)
    }
}
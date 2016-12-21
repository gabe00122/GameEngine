package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.VisTextButton
import gabek.sm2.Screen
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class SettingsMenu : Screen(){
    init{
        val backBut = VisTextButton("Back")
        backBut.onChange { changeEvent, visTextButton ->
            manager.show("main")
        }

        val con = Container<VisTextButton>(backBut)
        con.setFillParent(true)
        root.addActor(con)
    }
}
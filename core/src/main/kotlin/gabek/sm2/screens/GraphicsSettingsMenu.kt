package gabek.sm2.screens

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.*
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.screens.Screen
import gabek.sm2.settings.Settings
import gabek.sm2.ui.MenuControl
import ktx.actors.onChange

/**
* @author Gabriel Keith
* @date 3/15/2017
*/
class GraphicsSettingsMenu(kodein: Kodein): Screen(){
    init{
        val setting: Settings = kodein.instance()
        val uiScale = setting.getFloatValue("ui_scale")

        val uiScaleX1 = VisTextButton("UI Size 1")
        uiScaleX1.onChange { _, _ ->
            uiScale.value = 1.75f
        }

        val uiScaleX2 = VisTextButton("UI Size 2")
        uiScaleX2.onChange { _, _ ->
            uiScale.value = 2.25f
        }

        val uiScaleX3 = VisTextButton("UI Size 3")
        uiScaleX3.onChange { _, _ ->
            uiScale.value = 3.0f
        }

        val uiScaleX4 = VisTextButton("UI Size 4")
        uiScaleX4.onChange { _, _ ->
            uiScale.value = 4.0f
        }

        val choiceBox = VisSelectBox<String>()
        choiceBox.setItems("One", "Two", "Three")

        val back = VisTextButton("Back")
        back.onChange { _, _ ->
            manager.show("settings")
        }

        val menuControl = MenuControl(uiScaleX1, uiScaleX2, uiScaleX3, uiScaleX4, back)
        menuControl.playerInput = kodein.instance<PlayerInputManager>()

        val window = VisWindow("Graphics")
        window.isMovable = false
        window.add(choiceBox).row()
        window.add(menuControl)

        val container = Container(window)
        container.setFillParent(true)
        root.addActor(container)
    }
}
package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.salomonbrys.kodein.Kodein
import com.kotcrab.vis.ui.widget.VisTextButton
import gabek.sm2.ScreenManager
import gabek.sm2.input.MenuControl

/**
 * @author Gabriel Keith
 */

val buttonWidth = 80f
val eagePad = 7f
val menuPad = 5f

fun buildScreenManager(kodein: Kodein): ScreenManager{
    val sm = ScreenManager(kodein)
    with(sm){
        bind("main"){ MainMenu(kodein) }
        bind("settings"){ SettingsMenu() }
        bind("playing"){ PlayingScreen(kodein) }
        bind("startGame"){ StartGameScreen(kodein) }

        show("main")
    }
    return sm
}
package gabek.sm2.screens

import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 */

val buttonWidth = 80f
val eagePad = 7f
val menuPad = 5f

fun buildScreenManager(kodein: Kodein): ScreenManager {
    val sm = ScreenManager(kodein)
    with(sm) {
        bind("main") { MainMenu(kodein) }
        bind("settings") { SettingsMenu(kodein) }
        bind("playing") { PlayingScreen(kodein) }
        bind("startGame") { GameSetupScreen(kodein) }

        show("main")
    }
    return sm
}
package gabek.spacemonk.screen

import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.screen.*

/**
 * @author Gabriel Keith
 */

fun buildScreenManager(kodein: Kodein): ScreenManager {
    val sm = ScreenManager(kodein)
    with(sm) {
        bind("main") { MainMenu(kodein) }
        bind("settings") { SettingsMenu(kodein) }
        bind("settings_graphics") { GraphicsSettingsMenu(kodein) }
        bind("settings_sound") { SoundSettingsMenu(kodein) }
        bind("settings_controller") { ControllerSettingsMenus(kodein) }
        bind("playing") { PlayingScreen(kodein) }
        bind("startGame") { GameSetupScreen(kodein) }

        show("main")
    }
    return sm
}
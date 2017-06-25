package gabek.onebreath

import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.screen.ScreenManager
import gabek.onebreath.screen.GameViewScreen
import gabek.onebreath.screen.LoadingScreen

/**
 * @another Gabriel Keith
 * @date 5/16/2017.
 */

fun buildScreenManager(kodein: Kodein): ScreenManager {
    val screenManager = ScreenManager(kodein)

    screenManager.bind("GameViewScreen", { GameViewScreen(kodein) })
    screenManager.bind("LoadingScreen", { LoadingScreen(kodein) })
    screenManager.show("LoadingScreen")

    return screenManager
}
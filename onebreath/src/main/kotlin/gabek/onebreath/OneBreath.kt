package gabek.onebreath

import com.artemis.World
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import gabek.engine.core.RGame
import gabek.engine.core.assets.Assets
import gabek.engine.core.audio.MusicPlayer
import gabek.engine.core.audio.SoundPlayer
import gabek.engine.core.console.Console
import gabek.engine.core.graphics.PixelRatio
import gabek.engine.core.input.InputManager
import gabek.engine.core.screen.Screen
import gabek.engine.core.screen.ScreenManager
import gabek.engine.core.settings.Settings
import gabek.engine.core.screen.splash.GenaricSplashScreen
import gabek.engine.core.util.symbol.SymbolManager
import gabek.onebreath.def.buildInputManager
import gabek.onebreath.def.buildScreenManager
import gabek.onebreath.def.buildWorld


/**
* @another Gabriel Keith
* @date 5/16/2017.
*/

class OneBreath: RGame() {
    override fun kodeinSetup(builder: Kodein.Builder) = with(builder) {
        bind<Batch>() with singleton { SpriteBatch() }
        bind<Screen>("splash") with singleton { GenaricSplashScreen() }

        bind<SymbolManager>() with singleton { SymbolManager() }

        bind<ScreenManager>() with singleton { buildScreenManager(kodein) }
        bind<Assets>() with singleton { Assets() }
        bind<Settings>() with singleton { Settings("./onebreath.pref") }
        bind<MusicPlayer>() with singleton { MusicPlayer(this) }
        bind<SoundPlayer>() with singleton { SoundPlayer() }
        bind<InputManager>() with singleton { buildInputManager() }

        bind<PixelRatio>() with singleton { PixelRatio(0.75f/16f) }
        bind<World>() with singleton { buildWorld(this) }

        bind<Console>() with singleton { Console(this) }
    }

    override fun dispose() {
        kodein.instance<Assets>().dispose()
        kodein.instance<Settings>().save()
        kodein.instance<ScreenManager>().dispose()
        kodein.instance<World>().dispose()
    }
}
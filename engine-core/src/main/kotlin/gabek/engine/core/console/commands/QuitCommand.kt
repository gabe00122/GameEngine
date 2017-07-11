package gabek.engine.core.console.commands

import com.badlogic.gdx.Gdx
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console

/**
 * @author Gabriel Keith
 */
class QuitCommand: Command(){
    override fun process(args: Array<String>, console: Console, kodein: Kodein) {
        Gdx.app.exit()
    }
}
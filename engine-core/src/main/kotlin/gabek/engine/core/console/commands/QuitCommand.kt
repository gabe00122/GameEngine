package gabek.engine.core.console.commands

import com.badlogic.gdx.Gdx
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.console.Command
import gabek.engine.core.console.HelpLevel

/**
 * @author Gabriel Keith
 */
class QuitCommand: Command(){
    override fun setup(kodein: Kodein) {}

    override fun process(args: Array<String>) {
        Gdx.app.exit()
    }

    override fun help(level: HelpLevel) {}
}
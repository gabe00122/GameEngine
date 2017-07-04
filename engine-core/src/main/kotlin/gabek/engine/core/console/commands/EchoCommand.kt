package gabek.engine.core.console.commands

import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EchoCommand: Command() {
    override fun process(args: Array<String>, console: Console, kodein: Kodein) {
        console.writeln(args.joinToString())
    }
}
package gabek.engine.core.console.commands

import gabek.engine.core.console.Command
import gabek.engine.core.console.Console

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EchoCommand(console: Console) : Command(console, "echo") {


    override fun command(arguments: String) {
        console.writeln(arguments)
    }
}
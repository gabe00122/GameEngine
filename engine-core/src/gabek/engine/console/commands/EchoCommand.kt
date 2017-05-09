package gabek.engine.console.commands

import gabek.engine.console.Command
import gabek.engine.console.Console

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EchoCommand(console: Console) : Command(console, "echo") {


    override fun command(arguments: String) {
        console.writeln(arguments)
    }
}
package gabek.sm2.console.commands

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EchoCommand(console: gabek.sm2.console.Console) : gabek.sm2.console.Command(console, "echo") {


    override fun command(arguments: String) {
        console.writeln(arguments)
    }
}
package gabek.engine.console.commands

import com.github.salomonbrys.kodein.instance
import gabek.engine.console.Command
import gabek.engine.console.Console

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */

class DeleteCommand(console: Console) : Command(console, "delete") {
    val world = console.kodein.instance<com.artemis.World>()

    override fun command(arguments: String) {
        val id = arguments.toInt()
        world.delete(id)
    }
}
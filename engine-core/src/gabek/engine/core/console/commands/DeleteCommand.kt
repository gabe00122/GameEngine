package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */

class DeleteCommand(console: Console) : Command(console, "delete") {
    val world = console.kodein.instance<World>()

    override fun command(arguments: String) {
        val id = arguments.toInt()
        world.delete(id)
    }
}
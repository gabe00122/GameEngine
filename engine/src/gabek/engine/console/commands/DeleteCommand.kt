package gabek.sm2.console.commands

import com.github.salomonbrys.kodein.instance

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */

class DeleteCommand(console: gabek.sm2.console.Console) : gabek.sm2.console.Command(console, "delete") {
    val world = console.kodein.instance<com.artemis.World>()

    override fun command(arguments: String) {
        val id = arguments.toInt()
        world.delete(id)
    }
}
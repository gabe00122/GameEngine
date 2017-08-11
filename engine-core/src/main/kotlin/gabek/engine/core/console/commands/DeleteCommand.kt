package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.console.HelpLevel

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */


class DeleteCommand : Command() {
    private lateinit var world: World


    override fun setup(kodein: Kodein) {
        world = kodein.instance()
    }

    override fun process(args: Array<String>) {
        val id = args[0].toInt()
        world.delete(id)
    }

    override fun help(level: HelpLevel) {

    }
}

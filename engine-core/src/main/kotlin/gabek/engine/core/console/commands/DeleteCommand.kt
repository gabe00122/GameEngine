package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */


class DeleteCommand : Command() {

    override fun process(args: Array<String>, console: Console, kodein: Kodein) {
        val world: World = kodein.instance()

        val id = args[0].toInt()
        world.delete(id)
    }
}

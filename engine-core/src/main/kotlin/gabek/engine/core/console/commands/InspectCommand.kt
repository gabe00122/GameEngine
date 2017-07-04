package gabek.engine.core.console.commands

import com.artemis.Component
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */

/*
class InspectCommand(console: Console): Command(console, "inspect") {
    val world: com.artemis.World = console.kodein.instance()

    override fun command(arguments: String) {
        val bag = com.artemis.utils.Bag<Component>()
        world.componentManager.getComponentsFor(arguments.toInt(), bag)

        for (i in 0 until bag.size()) {
            console.writeln(bag[i].toString())
        }
    }
}
*/
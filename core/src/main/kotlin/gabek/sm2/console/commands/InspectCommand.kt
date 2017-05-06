package gabek.sm2.console.commands

import com.artemis.Component
import com.artemis.World
import com.artemis.utils.Bag
import com.github.salomonbrys.kodein.instance
import gabek.sm2.console.Command
import gabek.sm2.console.Console

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */
class InspectCommand(console: Console) : Command(console, "inspect") {
    val world: World = console.kodein.instance()

    override fun command(arguments: String) {
        val bag = Bag<Component>()
        world.componentManager.getComponentsFor(arguments.toInt(), bag)

        for (i in 0 until bag.size()) {
            console.writeln(bag[i].toString())
        }
    }
}
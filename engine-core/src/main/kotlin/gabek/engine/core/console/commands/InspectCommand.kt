package gabek.engine.core.console.commands

import com.artemis.Component
import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.console.HelpLevel

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */


class InspectCommand: Command() {
    private lateinit var world: World

    override fun setup(kodein: Kodein) {
        world = kodein.instance()
    }

    override fun process(args: Array<String>) {
        val bag = com.artemis.utils.Bag<Component>()
        world.componentManager.getComponentsFor(args[0].toInt(), bag)

        for (i in 0 until bag.size()) {
            console.writeln(bag[i].toString())
        }
    }

    override fun help(level: HelpLevel) {

    }
}

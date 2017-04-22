package gabek.sm2.console

import com.artemis.Component
import com.artemis.World
import com.artemis.utils.Bag
import com.github.salomonbrys.kodein.instance

/**
 * @author Gabriel Keith
 * @date 4/19/2017
 */
class InspectCommand(console: Console): Command(console, "inspect"){
    val world: World = console.kodein.instance()

    override fun command(arguments: String) {
        val bag = Bag<Component>()
        world.componentManager.getComponentsFor(arguments.toInt(), bag)

        val output = StringBuilder()
        for(i in 0 until bag.size()){
            val comp = bag[i]
            output.appendln(comp)
        }
        console.write(output.toString())
    }
}
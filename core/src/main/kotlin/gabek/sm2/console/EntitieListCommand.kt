package gabek.sm2.console

import com.artemis.Aspect
import com.artemis.World
import com.github.salomonbrys.kodein.instance

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EntitieListCommand(console: Console): Command(console, "list"){
    val world: World = console.kodein.instance()

    override fun command(arguments: String) {
        val bag = world.aspectSubscriptionManager.get(Aspect.all()).entities
        val output = StringBuilder()

        for(i in 0 until bag.size()){
            val entity = bag[i]
            output.append("$entity\n")
        }

        console.write(output.toString())
    }
}
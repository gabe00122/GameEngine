package gabek.engine.core.console.commands

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.systems.common.TranslationSystem
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */
class TeleportCommand(console: Console): Command(console, "teleport"){
    val world: com.artemis.World = console.kodein.instance()
    val transSystem: TranslationSystem = world.getSystem()

    override fun command(arguments: String) {
        val split = arguments.split(" ")

        //f(arguments.length == 3){

            val ent = split[0].toInt()
            val x = split[1].toFloat()
            val y = split[2].toFloat()

            transSystem.teleport(ent, x, y, 0f)
        //}
    }

}
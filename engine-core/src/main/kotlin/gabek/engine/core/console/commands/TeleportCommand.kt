package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.systems.common.TranslationSystem
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */


class TeleportCommand: Command(){
    override fun process(args: Array<String>, console: Console, kodein: Kodein) {
        val world: World = kodein.instance()
        val transSystem: TranslationSystem = world.getSystem()

        //f(arguments.length == 3){

            val ent = args[0].toInt()
            val x = args[1].toFloat()
            val y = args[2].toFloat()

            transSystem.teleport(ent, x, y, 0f)
        //}
    }

}

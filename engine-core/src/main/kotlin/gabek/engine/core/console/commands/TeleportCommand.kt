package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.console.HelpLevel
import gabek.engine.core.systems.common.TranslationSystem
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 6/4/2017
 */


class TeleportCommand: Command(){
    private lateinit var world: World
    private lateinit var transSystem: TranslationSystem

    override fun setup(kodein: Kodein) {
        world = kodein.instance()
        transSystem = world.getSystem()
    }

    override fun process(args: Array<String>) {
        val ent = args[0].toInt()
        val x = args[1].toFloat()
        val y = args[2].toFloat()

        transSystem.setPosition(ent, x, y)
    }

    override fun help(level: HelpLevel) {

    }

}

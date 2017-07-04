package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.systems.graphics.Box2dDebugSystem
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 5/5/2017
 */


class Box2dOverlayCommand: Command() {
    //val debugSystem: Box2dDebugSystem = kodein.instance<World>().getSystem()

    override fun process(args: Array<String>, console: Console, kodein: Kodein) {
        val debugSystem: Box2dDebugSystem = kodein.instance<World>().getSystem()

        if(args[0] == "on"){
            debugSystem.active = true
        } else if(args[0] == "off"){
            debugSystem.active = false
        }
    }
}

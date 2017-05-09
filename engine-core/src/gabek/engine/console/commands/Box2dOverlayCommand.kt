package gabek.engine.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.instance
import gabek.engine.console.Command
import gabek.engine.console.Console
import gabek.engine.systems.graphics.Box2dDebugSystem
import gabek.engine.util.getSystem

/**
 * @author Gabriel Keith
 * @date 5/5/2017
 */

class Box2dOverlayCommand(console: Console): Command(console, "b2d") {
    val debugSystem: Box2dDebugSystem = kodein.instance<World>().getSystem()

    override fun command(arguments: String) {


        if(arguments == "on"){
            debugSystem.active = true

        } else if(arguments == "off"){
            debugSystem.active = false
        }
    }
}
package gabek.sm2.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.instance
import gabek.sm2.console.Command
import gabek.sm2.console.Console
import gabek.sm2.systems.graphics.Box2dDebugSystem
import gabek.sm2.util.getSystem

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
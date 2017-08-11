package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.console.HelpLevel
import gabek.engine.core.systems.graphics.Box2dDebugSystem
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 5/5/2017
 */


class Box2dOverlayCommand: Command() {
    private lateinit var world: World
    private lateinit var debugSystem: Box2dDebugSystem

    override fun setup(kodein: Kodein) {
        world = kodein.instance()
        debugSystem = world.getSystem()
    }

    override fun process(args: Array<String>) {
        if(args[0] == "on"){
            debugSystem.active = true
        } else if(args[0] == "off"){
            debugSystem.active = false
        }
    }


    override fun help(level: HelpLevel) {

    }
}

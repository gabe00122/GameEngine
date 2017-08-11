package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.console.HelpLevel
import gabek.engine.core.systems.common.PrefabManager
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */


class CreateCommand : Command() {
    private lateinit var world: World
    private lateinit var prefabManager: PrefabManager

    override fun setup(kodein: Kodein) {
        world = kodein.instance()
        prefabManager = world.getSystem()
    }

    override fun process(args: Array<String>) {
        val prefab = args[0]
        val x = args[1].toFloat()
        val y = args[2].toFloat()

        prefabManager.getPrefab(prefab).create(x, y)
    }

    override fun help(level: HelpLevel) {

    }
}

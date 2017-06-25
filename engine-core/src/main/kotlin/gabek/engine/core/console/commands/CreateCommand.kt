package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.systems.common.PrefabManager
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */
class CreateCommand(console: Console) : Command(console, "create") {
    val prefabManager: PrefabManager = console.kodein.instance<World>().getSystem()

    override fun command(arguments: String) {
        val split = arguments.split(" ")


        val prefab = split[0]
        val x = split[1].toFloat()
        val y = split[2].toFloat()

        prefabManager.getPrefab(prefab).create(x, y)
    }
}
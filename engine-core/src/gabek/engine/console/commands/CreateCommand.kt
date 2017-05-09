package gabek.engine.console.commands

import com.github.salomonbrys.kodein.instance
import gabek.engine.console.Command
import gabek.engine.console.Console
import gabek.engine.systems.common.PrefabManager
import gabek.engine.util.getSystem

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */
class CreateCommand(console: Console) : Command(console, "create") {
    val prefabManager = console.kodein.instance<com.artemis.World>().getSystem<PrefabManager>()

    override fun command(arguments: String) {
        val split = arguments.split(" ")


        val prefab = split[0]
        val x = split[1].toFloat()
        val y = split[2].toFloat()

        prefabManager.getPrefab(prefab).create(x, y)
    }
}
package gabek.sm2.console.commands

import com.github.salomonbrys.kodein.instance
import gabek.sm2.util.getSystem

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */
class CreateCommand(console: gabek.sm2.console.Console) : gabek.sm2.console.Command(console, "create") {
    val prefabManager = console.kodein.instance<com.artemis.World>().getSystem<gabek.sm2.systems.common.PrefabManager>()

    override fun command(arguments: String) {
        val split = arguments.split(" ")


        val prefab = split[0]
        val x = split[1].toFloat()
        val y = split[2].toFloat()

        prefabManager.getPrefab(prefab).create(x, y)
    }
}
package gabek.sm2.console

import com.artemis.World
import com.github.salomonbrys.kodein.instance
import gabek.sm2.systems.common.PrefabManager
import gabek.sm2.world.getSystem

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */
class CreateCommand(console: Console): Command(console, "create"){
    val prefabManager = console.kodein.instance<World>().getSystem<PrefabManager>()

    override fun command(arguments: String) {
        val split = arguments.split(" ")


        val prefab = split[0]
        val x = split[1].toFloat()
        val y = split[2].toFloat()

        prefabManager.getPrefab(prefab).create(x, y)
    }
}
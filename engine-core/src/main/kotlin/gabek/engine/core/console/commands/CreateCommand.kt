package gabek.engine.core.console.commands

import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.systems.common.PrefabManager
import gabek.engine.core.util.getSystem

/**
 * @author Gabriel Keith
 * @date 4/20/2017
 */


class CreateCommand : Command() {
    override fun process(args: Array<String>, console: Console, kodein: Kodein) {
        val prefabManager = console.kodein.instance<World>().getSystem<PrefabManager>()

        val prefab = args[0]
        val x = args[1].toFloat()
        val y = args[2].toFloat()

        prefabManager.getPrefab(prefab).create(x, y)
    }
}

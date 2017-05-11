package gabek.engine.core.console.commands

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.World
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.components.meta.PrefabCom
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.util.getMapper

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EntitieListCommand(console: Console) : Command(console, "list") {
    val world: World = console.kodein.instance()
    val prefabMapper: ComponentMapper<PrefabCom> = world.getMapper()

    override fun command(arguments: String) {
        val bag = world.aspectSubscriptionManager.get(Aspect.all()).entities

        for (i in 0 until bag.size()) {
            val entity = bag[i]
            console.write("$entity")

            if(prefabMapper.has(entity)){
                console.write(": ${prefabMapper[entity].name}")
            }

            console.write("\n")
        }

    }
}
package gabek.sm2.console.commands

import com.artemis.ComponentMapper
import com.github.salomonbrys.kodein.instance
import gabek.sm2.components.meta.PrefabCom
import gabek.sm2.util.getMapper

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EntitieListCommand(console: gabek.sm2.console.Console) : gabek.sm2.console.Command(console, "list") {
    val world: com.artemis.World = console.kodein.instance()
    val prefabMapper: ComponentMapper<PrefabCom> = world.getMapper()

    override fun command(arguments: String) {
        val bag = world.aspectSubscriptionManager.get(com.artemis.Aspect.all()).entities

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
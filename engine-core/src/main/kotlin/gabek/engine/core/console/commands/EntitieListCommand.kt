package gabek.engine.core.console.commands

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.World
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.components.meta.PrefabCom
import gabek.engine.core.console.Command
import gabek.engine.core.console.Console
import gabek.engine.core.console.HelpLevel
import gabek.engine.core.util.getMapper

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */


class EntitieListCommand : Command() {
    private lateinit var world: World
    private lateinit var prefabMapper: ComponentMapper<PrefabCom>

    override fun setup(kodein: Kodein) {
        world = kodein.instance()
        prefabMapper = world.getMapper()
    }

    override fun process(args: Array<String>) {
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

    override fun help(level: HelpLevel) {

    }
}

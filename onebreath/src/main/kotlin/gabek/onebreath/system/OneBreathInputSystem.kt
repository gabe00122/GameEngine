package gabek.onebreath.system

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.InputCom
import gabek.engine.core.components.channel.DirectionalInputCom
import gabek.onebreath.def.ActionCodes

/**
 * @another Gabriel Keith
 * @date 5/23/2017.
 */

class OneBreathInputSystem: BaseEntitySystem(Aspect.all(InputCom::class.java)){
    private lateinit var inputMapper: ComponentMapper<InputCom>
    private lateinit var directionalInputMapper: ComponentMapper<DirectionalInputCom>

    override fun processSystem() {
        val entities = entityIds

        for(i in 0 until entities.size()){
            val entity = entities[i]
            val input = inputMapper[entity].input

            if(input != null) {
                if (directionalInputMapper.has(entity)) {
                    val dirInput = directionalInputMapper[entity]

                    dirInput.panX = if(input.pollAction(ActionCodes.RIGHT)) 1f else 0f
                    dirInput.panX += if(input.pollAction(ActionCodes.LEFT)) -1f else 0f

                    dirInput.panY = if(input.pollAction(ActionCodes.UP)) 1f else 0f
                    dirInput.panY += if(input.pollAction(ActionCodes.DOWN)) -1f else 0f
                }
            }
        }
    }
}
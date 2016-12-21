package gabek.sm2.world

import com.artemis.World
import com.artemis.link.LinkAdapter

/**
 * @author Gabriel Keith
 */
class DeleteChild : LinkAdapter(){
    lateinit var world: World

    override fun onLinkKilled(sourceId: Int, targetId: Int) {
        if(targetId != -1){
            world.delete(targetId)
        }
    }
}
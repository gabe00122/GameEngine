package gabek.sm2.systems.character

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.BaseSystem
import com.artemis.EntitySubscription
import com.artemis.utils.IntBag
import gabek.sm2.components.PlayerInputCom

/**
 * @author Gabriel Keith
 * @date 3/28/2017
 */
class PlayerManager: BaseEntitySystem(Aspect.all(PlayerInputCom::class.java)){

    private var onAllPlayersDead: (() -> Unit)? = null

    override fun processSystem() {}


    fun getPlayerList(): IntBag{
        return subscription.entities
    }

    override fun removed(entities: IntBag) {
        super.removed(entities)
        if(getPlayerList().size() - entities.size() <= 0){
            onAllPlayersDead?.invoke()
        }
    }

    fun onAllPlayersDead(onAllPlayersDead: () -> Unit){
        this.onAllPlayersDead = onAllPlayersDead
    }
}
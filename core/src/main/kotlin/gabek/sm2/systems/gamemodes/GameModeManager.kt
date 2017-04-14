package gabek.sm2.systems.gamemodes

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.utils.IntBag
import gabek.sm2.components.PlayerInputCom

/**
 * @author Gabriel Keith
 * @date 3/28/2017
 */
class GameModeManager : BaseEntitySystem(Aspect.all(PlayerInputCom::class.java)){

    private var onGameOver: (() -> Unit)? = null

    override fun processSystem() {}


    val playerList get() = entityIds!!

    override fun removed(entities: IntBag) {
        super.removed(entities)
        if(playerList.size() == 0){
            onGameOver?.invoke()
        }
    }

    fun onGameOver(onGameOver: () -> Unit){
        this.onGameOver = onGameOver
    }
}
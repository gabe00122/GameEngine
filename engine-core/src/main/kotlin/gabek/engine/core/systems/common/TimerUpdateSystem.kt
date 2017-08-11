package gabek.engine.core.systems.common

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.badlogic.gdx.utils.Pool
import gabek.engine.core.util.poolOf
import java.util.*

/**
 * @author Gabriel Keith
 * @date 7/27/2017
 */
abstract class TimerUpdateSystem(aspect: Aspect.Builder): BaseEntitySystem(aspect){
    private lateinit var updateManager: UpdateManager

    private val priorityQueue = PriorityQueue<Event>()
    private val eventPool = poolOf { Event() }

    override fun processSystem() {
        val currentTime = updateManager.currentTime
        while(priorityQueue.isNotEmpty() && priorityQueue.peek().targetTime <= currentTime){
            val event = priorityQueue.poll()
            val entity = event.entityId
            if(subscription.activeEntityIds[entity]){
                onCheck(entity)
            }

            eventPool.free(event)
        }
    }

    fun checkAt(entity: Int, time: Float){
        val event = eventPool.obtain()
        event.entityId = entity
        event.targetTime = time
        priorityQueue.add(event)
    }

    abstract fun onCheck(entity: Int)

    private class Event: Comparable<Event>, Pool.Poolable{
        var entityId: Int = -1
        var targetTime: Float = 0f

        override fun compareTo(other: Event) = targetTime.compareTo(other.targetTime)

        override fun reset() {
            entityId = -1
            targetTime = 0f
        }
    }
}
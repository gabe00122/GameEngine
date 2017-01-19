package gabek.sm2

import com.artemis.BaseSystem
import com.artemis.World
import com.artemis.utils.ArtemisProfiler
import com.badlogic.gdx.Gdx

/**
 * @author Gabriel Keith
 */
class Profiler: ArtemisProfiler{
    private var startTime: Long = 0

    private var sumTime: Long = 0
    private var frame: Long = 0

    override fun start() {
        startTime = System.nanoTime()
    }

    override fun stop() {
        val delta = System.nanoTime() - startTime
        frame++
        sumTime += delta

        if(sumTime > 100000000) {
            Gdx.app.log("Time", "${sumTime / (frame * 1000000.0)}")
            sumTime = 0
            frame = 0
        }
    }

    override fun initialize(owner: BaseSystem, world: World) {

    }
}
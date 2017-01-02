package gabek.sm2.world

import com.artemis.World

/**
 * @author Gabriel Keith
 */
class UpdateManager(val world: World, val frequency: Float) {
    val step: Float = 1 / frequency
    var accumulator: Float = 0f
    val progress: Float
        get() = accumulator / step

    init {
        world.delta = step
    }

    fun update(delta: Float) {
        accumulator += delta
        while (accumulator > step) {
            world.process()
            accumulator -= step
        }
    }
}
package gabek.engine.core.physics.shape

import com.badlogic.gdx.physics.box2d.Shape

/**
 * @author Gabriel Keith
 */
abstract class RShape: Cloneable {
    var shape: Shape? = null
        protected set

    abstract fun preInit()
    open fun postInit() {
        shape?.dispose()
        shape = null
    }
}
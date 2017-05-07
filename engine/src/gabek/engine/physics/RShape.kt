package gabek.sm2.physics

import com.badlogic.gdx.physics.box2d.Shape

/**
 * @author Gabriel Keith
 */
abstract class RShape {
    var shape: Shape? = null
        protected set

    abstract fun clone(): RShape

    abstract fun preInit()
    open fun postInit() {
        shape?.dispose()
        shape = null
    }
}
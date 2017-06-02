package gabek.engine.core.physics.shape

import com.badlogic.gdx.physics.box2d.Shape
import gabek.engine.core.util.Cloneable

/**
 * @author Gabriel Keith
 */
abstract class RShape: Cloneable<RShape> {

    internal abstract fun generate(): Shape

}
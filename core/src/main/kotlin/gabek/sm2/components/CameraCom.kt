package gabek.sm2.components

import com.artemis.Component
import com.artemis.annotations.PooledWeaver

/**
 * @author Gabriel Keith
 */
@PooledWeaver
class CameraCom : Component(){
    @JvmField var viewportWidth: Float = 0f
    @JvmField var viewportHeight: Float = 0f
}
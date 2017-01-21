package gabek.sm2.components.graphics

import com.artemis.Component
import gabek.sm2.util.LerpVector

/**
 * @author Gabriel Keith
 */
class CameraCom : Component() {
    var bottomLeft = LerpVector()
    var topRight = LerpVector()

    var aspectRatio: Float = 0f
}
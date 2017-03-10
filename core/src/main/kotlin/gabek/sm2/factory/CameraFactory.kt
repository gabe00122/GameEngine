package gabek.sm2.factory

import gabek.sm2.components.graphics.CameraCom
import gabek.sm2.components.graphics.CameraTargetsCom

/**
 * @author Gabriel Keith
 */


fun cameraFactory() = factory { kodein, world ->
    val width = 5f
    val height = 5f

    com<CameraCom>()
    com<CameraTargetsCom> {
        padWidth = width
        padHeight = height
    }
}
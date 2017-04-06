package gabek.sm2.factory

import gabek.sm2.components.graphics.CameraCom
import gabek.sm2.components.graphics.CameraTargetsCom

/**
 * @author Gabriel Keith
 */


class CameraFactory: EntityFactory(){
    override fun define() {
        val width = 5f
        val height = 5f

        com<CameraCom>()
        com<CameraTargetsCom> {
            padWidth = width
            padHeight = height
        }
    }
}
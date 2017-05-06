package gabek.sm2.prefab

import gabek.sm2.components.common.BoundCom
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.components.graphics.CameraCom
import gabek.sm2.components.graphics.CameraTargetsCom

/**
 * @author Gabriel Keith
 */


class CameraPrefab : Prefab() {
    override fun define() {
        super.define()

        val width = 5f
        val height = 5f

        add<CameraCom>()
        add<CameraTargetsCom> {
            padWidth = width
            padHeight = height
        }
        add<TranslationCom>()
        add<BoundCom>()
    }
}
package gabek.engine.prefab

import gabek.engine.components.common.BoundCom
import gabek.engine.components.common.TranslationCom
import gabek.engine.components.graphics.CameraCom
import gabek.engine.components.graphics.CameraTargetsCom

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
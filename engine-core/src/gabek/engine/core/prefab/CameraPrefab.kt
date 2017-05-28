package gabek.engine.core.prefab

import gabek.engine.core.components.common.BoundCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.CameraCom
import gabek.engine.core.components.graphics.CameraTargetsCom

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
            padWidth = width / 2f
            padHeight = height / 2f
        }
        add<TranslationCom>()
        add<BoundCom>()
    }
}
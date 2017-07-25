package gabek.engine.core.prefab

import com.github.salomonbrys.kodein.instance
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.common.VelocityCom
import gabek.engine.core.components.graphics.CameraCom
import gabek.engine.core.components.graphics.CameraTargetsCom
import gabek.engine.core.graphics.PixelRatio

/**
 * @author Gabriel Keith
 */


class CameraPrefab : Prefab() {
    override fun define() {
        super.define()

        val pixelToMeters = kodein.instance<PixelRatio>().pixelToMeters

        add<CameraCom>{
            pixelAlignment = pixelToMeters
        }
        add<CameraTargetsCom>()
        add<TranslationCom>()
        add<VelocityCom>()
        add<SizeCom>{
            width = 5f
            height = 5f
        }
    }
}
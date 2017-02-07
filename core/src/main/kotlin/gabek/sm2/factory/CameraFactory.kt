package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.components.graphics.CameraCom
import gabek.sm2.components.graphics.CameraTargetsCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */


val cameraFactory = factory { kodein, world ->
    val width = 5f
    val height = 5f

    com<CameraCom>()
    com<CameraTargetsCom> {
        padWidth = width
        padHeight = height
    }
}
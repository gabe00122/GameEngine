package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.sm2.components.CameraCom
import gabek.sm2.components.CameraTargetsCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class CameraTrackingSystem : BaseEntitySystem(Aspect.all(
        TranslationCom::class.java,
        CameraCom::class.java,
        CameraTargetsCom::class.java)) {

    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var cameraMapper: ComponentMapper<CameraCom>
    private lateinit var cameraTargetsMapper: ComponentMapper<CameraTargetsCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val cameraTargets = cameraTargetsMapper[entity]
            if (!cameraTargets.targets.isEmpty) {
                val trans = transMapper[entity]
                val camera = cameraMapper[entity]
                val firstTarget = transMapper[cameraTargets.targets[0]]

                var x1 = firstTarget.x
                var y1 = firstTarget.y
                var x2 = firstTarget.x
                var y2 = firstTarget.y

                for (j in 1 until cameraTargets.targets.size()) {
                    val target = transMapper[cameraTargets.targets[j]]

                    if (target.x < x1) {
                        x1 = target.x
                    }
                    if (target.y < y1) {
                        y1 = target.y
                    }
                    if (target.x > x2) {
                        x2 = target.x
                    }
                    if (target.y > y2) {
                        y2 = target.y
                    }
                }

                x1 -= cameraTargets.padWidth / 2
                x2 += cameraTargets.padWidth / 2
                y1 -= cameraTargets.padHeight / 2
                y2 += cameraTargets.padHeight / 2

                trans.x = (x1 + x2) / 2
                trans.y = (y1 + y2) / 2
                camera.viewportWidth = x2 - x1
                camera.viewportHeight = y2 - y1
            }
        }
    }

}
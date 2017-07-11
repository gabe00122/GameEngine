package gabek.engine.core.systems.graphics

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import gabek.engine.core.components.common.SizeCom
import gabek.engine.core.components.common.TranslationCom
import gabek.engine.core.components.graphics.CameraCom
import gabek.engine.core.components.graphics.CameraTargetsCom

/**
 * @author Gabriel Keith
 */
class CameraTrackingSystem: BaseEntitySystem(Aspect.all(
        CameraCom::class.java,
        TranslationCom::class.java,
        SizeCom::class.java,
        CameraTargetsCom::class.java)) {

    private lateinit var transMapper: ComponentMapper<TranslationCom>
    private lateinit var sizeMapper: ComponentMapper<SizeCom>
    private lateinit var cameraMapper: ComponentMapper<CameraCom>
    private lateinit var cameraTargetsMapper: ComponentMapper<CameraTargetsCom>

    override fun processSystem() {
        val entities = entityIds

        for (i in 0 until entities.size()) {
            val entity = entities[i]

            val cameraTargets = cameraTargetsMapper[entity]
            if (!cameraTargets.targets.isEmpty) {
                val camera = cameraMapper[entity]
                val trans = transMapper[entity]
                val bounds = sizeMapper[entity]

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

                //cameraTargets.padWidth = (x2 - x1) * 0.25f
                //cameraTargets.padHeight = (y2 - y1) * 0.25f

                x1 -= cameraTargets.padWidth
                x2 += cameraTargets.padWidth
                y1 -= cameraTargets.padHeight
                y2 += cameraTargets.padHeight

                //trans.x = (x1 + x2) / 2
                //trans.y = (y1 + y2) / 2

                //val lagW = cameraTargets.padWidth * (5f / 8f)
                //val lagH = cameraTargets.padHeight * (5f / 8f)

                trans.x = (x1 + x2) / 2f // = MathUtils.clamp(camera.bottomLeft.x, x1, x1 + lagW)
                trans.y = (y1 + y2) / 2f // = MathUtils.clamp(camera.bottomLeft.y, y1, y1 + lagH)
                //bounds.width = x2 - x1 // = MathUtils.clamp(camera.topRight.x, x2 - lagW, x2)
                //bounds.height = y2 - y1 // = MathUtils.clamp(camera.topRight.y, y2 - lagH, y2)
            }
        }
    }

    fun setPadding(entity: Int, w: Float, h: Float){
        val targets = cameraTargetsMapper[entity]
        targets.padWidth = w
        targets.padHeight = h
    }

    fun addTarget(entity: Int, target: Int) {
        cameraTargetsMapper[entity].targets.add(target)
    }
}
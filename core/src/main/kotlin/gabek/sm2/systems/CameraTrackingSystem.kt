package gabek.sm2.systems

import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.math.MathUtils
import gabek.sm2.components.CameraCom
import gabek.sm2.components.CameraTargetsCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class CameraTrackingSystem : BaseEntitySystem(Aspect.all(
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
                val camera = cameraMapper[entity]
                val firstTarget = transMapper[cameraTargets.targets[0]]

                println(cameraTargets.targets[0])

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

                val lagW = cameraTargets.padWidth / 2f
                val lagH = cameraTargets.padHeight / 2f

                camera.bottomLeft.x = MathUtils.clamp(camera.bottomLeft.x, x1, x1 + lagW)
                camera.bottomLeft.y = MathUtils.clamp(camera.bottomLeft.y, y1, y1 + lagH)
                camera.topRight.x = MathUtils.clamp(camera.topRight.x, x2 - lagW, x2)
                camera.topRight.y = MathUtils.clamp(camera.topRight.y, y2 - lagH, y2)
            }
        }
    }

    fun addTarget(entity: Int, target: Int, safe: Boolean = false){
        if(!safe || (cameraTargetsMapper.has(entity) && transMapper.has(target))) {
            cameraTargetsMapper[entity].targets.add(target)
        }
    }

}
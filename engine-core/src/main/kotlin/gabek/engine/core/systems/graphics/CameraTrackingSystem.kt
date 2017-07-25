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
            val trans = transMapper[entity]
            val target = cameraTargetsMapper[entity].target

            if(target != -1 && transMapper.has(target)){
                val targetTrans = transMapper[target]

                trans.x = targetTrans.x
                trans.y = targetTrans.y
            }

        }
    }

    fun setTarget(entity: Int, target: Int) {
        cameraTargetsMapper[entity].target = target
    }
}
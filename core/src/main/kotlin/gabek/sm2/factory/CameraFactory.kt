package gabek.sm2.factory

import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.components.CameraCom
import gabek.sm2.components.CameraTargetsCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class CameraFactory(kodein: Kodein, val world: World) : EntityFactory {
    val arch = ArchetypeBuilder().add(TranslationCom::class.java, CameraCom::class.java, CameraTargetsCom::class.java).build(world)
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val cameraMapper = world.getMapper(CameraCom::class.java)
    private val cameraTargetMapper = world.getMapper(CameraTargetsCom::class.java)

    fun create(x: Float, y: Float, w: Float, h: Float): Int {
        val id = world.create(arch)
        val trans = transMapper.get(id)
        val camera = cameraMapper.get(id)
        val targets = cameraTargetMapper.get(id)

        trans.x = x
        trans.y = y
        camera.viewportWidth = w
        camera.viewportHeight = h

        targets.padWidth = 10f
        targets.padHeight = 10f

        return id
    }

    override fun dispose() {}
}
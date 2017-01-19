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
    val arch = ArchetypeBuilder().add(CameraCom::class.java, CameraTargetsCom::class.java).build(world)
    private val cameraTargetMapper = world.getMapper(CameraTargetsCom::class.java)

    fun create(): Int {
        val id = world.create(arch)
        val targets = cameraTargetMapper.get(id)

        targets.padWidth = 5f
        targets.padHeight = 5f

        return id
    }

    override fun dispose() {}
}
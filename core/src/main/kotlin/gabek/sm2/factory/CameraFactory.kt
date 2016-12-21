package gabek.sm2.factory

import com.artemis.Archetype
import com.artemis.ArchetypeBuilder
import com.artemis.ComponentMapper
import com.artemis.World
import com.badlogic.gdx.utils.Disposable
import com.github.salomonbrys.kodein.Kodein
import gabek.sm2.components.CameraCom
import gabek.sm2.components.TranslationCom

/**
 * @author Gabriel Keith
 */
class CameraFactory(kodein: Kodein, val world: World) : Disposable{
    val arch = ArchetypeBuilder().add(TranslationCom::class.java, CameraCom::class.java).build(world)
    private val transMapper = world.getMapper(TranslationCom::class.java)
    private val cameraMapper = world.getMapper(CameraCom::class.java)

    fun create(x: Float, y: Float, w: Float, h: Float): Int{
        val id = world.create(arch)
        val trans = transMapper.get(id)
        val camera = cameraMapper.get(id)

        trans.x = x
        trans.y = y
        camera.viewportWidth = w
        camera.viewportHeight = h

        return id
    }

    override fun dispose() {}
}
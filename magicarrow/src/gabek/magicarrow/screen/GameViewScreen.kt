package gabek.magicarrow.screen

import com.artemis.World
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import gabek.engine.core.graphics.Display
import gabek.engine.core.screen.Screen
import gabek.engine.core.systems.common.PrefabManager
import gabek.engine.core.systems.common.TranslationSystem
import gabek.engine.core.systems.common.UpdateManager
import gabek.engine.core.systems.graphics.CameraSystem
import gabek.engine.core.util.getSystem
import gabek.engine.core.world.RenderManager


/**
 * @another Gabriel Keith
 * @date 5/16/2017.
 */

class GameViewScreen(kodein: Kodein): Screen() {
    val display = Display(kodein)
    val world: World = kodein.instance()

    val updateManager: UpdateManager = world.getSystem()
    val renderManager: RenderManager = world.getSystem()

    override fun update(delta: Float) {
        super.update(delta)

        updateManager.update(delta)
    }

    override fun render(batch: SpriteBatch) {
        super.render(batch)

        renderManager.render(display, batch, updateManager.progress)
    }

    override fun show() {
        super.show()

        val prefabManager: PrefabManager = world.getSystem()
        val transSystem: TranslationSystem = world.getSystem()
        val cameraSystem: CameraSystem = world.getSystem()

        prefabManager.create("attacker_basic", 2f, 2f)

        val camera = prefabManager.create("camera")
        cameraSystem.setView(camera, 0f, 0f, 10f, 10f)


        display.setHandle(camera, cameraSystem)

        val container = Container(display)
        container.fill()
        container.setFillParent(true)

        root.addActor(container)
    }

    override fun dispose() {
        super.dispose()
        display.dispose()
    }
}
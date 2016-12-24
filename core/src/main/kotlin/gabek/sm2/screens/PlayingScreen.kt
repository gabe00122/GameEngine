package gabek.sm2.screens

import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.Align
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisLabel
import gabek.sm2.Assets
import gabek.sm2.Screen
import gabek.sm2.components.CameraTargetsCom
import gabek.sm2.components.ContactCom
import gabek.sm2.graphics.DisplayBuffer
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.factory.CameraFactory
import gabek.sm2.factory.JunkFactory
import gabek.sm2.factory.PlatformFactory
import gabek.sm2.factory.PlayerFactory
import gabek.sm2.systems.Box2dSystem
import gabek.sm2.systems.CameraSystem
import gabek.sm2.systems.RenderSystem
import gabek.sm2.world.UpdateManager

/**
 * @author Gabriel Keith
 */
class PlayingScreen(val kodein: Kodein) : Screen(){
    private val display = DisplayBuffer()
    private val world: World = kodein.instance()
    private val inputManager: PlayerInputManager = kodein.instance()

    private val updateManager = UpdateManager(world, 60f)
    private val renderSystem: RenderSystem
    private val cameraSystem: CameraSystem
    private val box2dSystem: Box2dSystem

    private val ortho = OrthographicCamera()
    private val box2dDebug = Box2DDebugRenderer(true, true, false, true, false, false)

    init {
        renderSystem = world.getSystem(RenderSystem::class.java)
        cameraSystem = world.getSystem(CameraSystem::class.java)
        box2dSystem = world.getSystem(Box2dSystem::class.java)
    }

    override fun show() {
        val cameraFactory = CameraFactory(kodein, world)
        val playerFactory = PlayerFactory(kodein, world)
        val platformFactory = PlatformFactory(kodein, world)
        val junkFactory = JunkFactory(kodein, world)

        val cam = cameraFactory.create(5f, 5f, 10f, 12f)
        val player = playerFactory.create(6f, 5f, inputManager.getPlayerInput(0))

        val targetMapper = world.getMapper(CameraTargetsCom::class.java)
        targetMapper[cam].targets.add(player)

        if(inputManager.playerInputSize > 1){
            val p = playerFactory.create(4f, 5f, inputManager.getPlayerInput(1))
            targetMapper[cam].targets.add(p)
        }

        junkFactory.create(1f, 3f, 0.5f, 0.5f)

        //platformFactory.create(0f, -3f, 10f, 1f)

        val displayContainer = Container<DisplayBuffer>(display).fill()
        displayContainer.setFillParent(true)
        root.addActor(displayContainer)
    }

    override fun update(delta: Float) {
        updateManager.update(delta)
    }

    override fun render(batch: SpriteBatch) {
        cameraSystem.updateProjection(ortho, updateManager.progress, display.width, display.height)
        batch.projectionMatrix = ortho.projection
        batch.transformMatrix = ortho.view

        display.beginPrimaryBuffer()
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)

        batch.begin()
        renderSystem.draw(batch, updateManager.progress)
        batch.end()

        //box2dDebug.render(box2dSystem.box2dWorld, ortho.combined)
        display.endPrimaryBuffer()
    }

    override fun dispose() {
        display.dispose()
        box2dDebug.dispose()
    }
}
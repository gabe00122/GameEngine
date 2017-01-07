package gabek.sm2.screens

import com.artemis.Aspect
import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.PlayerInfo
import gabek.sm2.WorldSetup
import gabek.sm2.components.CameraTargetsCom
import gabek.sm2.factory.CameraFactory
import gabek.sm2.factory.JunkFactory
import gabek.sm2.factory.PlatformFactory
import gabek.sm2.factory.PlayerFactory
import gabek.sm2.graphics.DisplayBuffer
import gabek.sm2.input.Actions
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.systems.Box2dSystem
import gabek.sm2.systems.CameraSystem
import gabek.sm2.systems.RenderSystem
import gabek.sm2.ui.MenuControl
import gabek.sm2.world.UpdateManager
import ktx.actors.onChange
import ktx.actors.onClick

/**
 * @author Gabriel Keith
 */
class PlayingScreen(val kodein: Kodein) : Screen() {
    private val display = DisplayBuffer()
    private val box2dDebug = Box2DDebugRenderer(true, true, false, true, false, false)

    private val world: World = kodein.instance()
    private val inputManager: PlayerInputManager = kodein.instance()
    private val worldSetup: WorldSetup = kodein.instance()

    private val updateManager = UpdateManager(world, 60f)
    private val renderSystem: RenderSystem
    private val cameraSystem: CameraSystem
    private val box2dSystem: Box2dSystem

    private val ortho = OrthographicCamera()

    private val pauseWindow: VisWindow = VisWindow("Pause")
    private val pauseContainer = Container(pauseWindow)
    private val pauseMenuControl: MenuControl

    init {
        renderSystem = world.getSystem(RenderSystem::class.java)
        cameraSystem = world.getSystem(CameraSystem::class.java)
        box2dSystem = world.getSystem(Box2dSystem::class.java)


        // pause menu
        pauseContainer.setFillParent(true)
        pauseWindow.isMovable = false
        val resumeBut = VisTextButton("Resume", "toggle")
        resumeBut.onChange { changeEvent, visTextButton -> isPaused = false }

        val quitBut = VisTextButton("Quit", "toggle")
        quitBut.onChange { changeEvent, visTextButton ->
            val bag = world.aspectSubscriptionManager.get(Aspect.all()).entities.data
            for(i in bag){
                world.delete(i)
            }
            world.process()
            world.entityManager.reset()

            manager.show("main")
        }

        pauseMenuControl = MenuControl(resumeBut, quitBut)
        pauseWindow.add(pauseMenuControl)
    }

    override fun show() {
        val cameraFactory = CameraFactory(kodein, world)
        val playerFactory = PlayerFactory(kodein, world)
        val platformFactory = PlatformFactory(kodein, world)
        val junkFactory = JunkFactory(kodein, world)
        val targetMapper = world.getMapper(CameraTargetsCom::class.java)

        val cam = cameraFactory.create(5f, 5f, 10f, 12f)

        for(i in 0 until worldSetup.players.size){
            val playerInfo: PlayerInfo = worldSetup.players[i]

            val id = playerFactory.create(2f, 2f + i, playerInfo.input)
            targetMapper[cam].targets.add(id)
        }

        for (i in 0..7) {
            for (j in i..15) {
                junkFactory.create(11f + i * 2f, 1f + j, 1f, 1f)
            }
        }
        //platformFactory.create(0f, -3f, 10f, 1f)

        val displayContainer = Container<DisplayBuffer>(display).fill()
        displayContainer.setFillParent(true)
        root.addActor(displayContainer)
    }

    private var isPaused: Boolean = false
        set(value) {
            field = value
            if(value){
                root.addActor(pauseContainer)
            } else {
                pauseMenuControl.playerInput = null
                root.removeActor(pauseContainer)
            }
        }

    override fun update(delta: Float) {
        if(!isPaused) {
            updateManager.update(delta)

            for (playerInfo in worldSetup.players) {
                if (playerInfo.input.pollAction(Actions.ESCAPE)) {
                    pauseMenuControl.playerInput = playerInfo.input
                    isPaused = true
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
                isPaused = true
            }
        } else {
            inputManager.update(delta)
        }
    }

    override fun render(batch: SpriteBatch) {
        if(!isPaused) {
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
    }

    override fun dispose() {
        display.dispose()
        box2dDebug.dispose()
    }
}
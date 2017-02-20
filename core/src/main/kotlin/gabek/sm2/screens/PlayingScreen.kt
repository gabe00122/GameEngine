package gabek.sm2.screens

import com.artemis.World
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.Assets
import gabek.sm2.PlayerInfo
import gabek.sm2.WorldSetup
import gabek.sm2.factory.cameraFactory
import gabek.sm2.factory.junkFactory
import gabek.sm2.factory.playerFactory
import gabek.sm2.graphics.DisplayBuffer
import gabek.sm2.input.Actions
import gabek.sm2.systems.FactoryManager
import gabek.sm2.systems.PlayerInputSystem
import gabek.sm2.systems.graphics.CameraTrackingSystem
import gabek.sm2.ui.MenuControl
import gabek.sm2.world.RenderManager
import gabek.sm2.world.UpdateManager
import gabek.sm2.world.clear
import gabek.sm2.world.getSystem
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class PlayingScreen(val kodein: Kodein) : Screen() {
    private val display = DisplayBuffer()

    private val world: World = kodein.instance()
    private val worldSetup: WorldSetup = kodein.instance()

    private val updateManager = UpdateManager(world, 60f)
    private val renderManager: RenderManager = kodein.instance()

    private val pauseWindow: VisWindow = VisWindow("Pause")
    private val pauseContainer = Container(pauseWindow)
    private val pauseMenuControl: MenuControl

    init {
        // pause menu
        pauseContainer.setFillParent(true)
        pauseWindow.isMovable = false
        val resumeBut = VisTextButton("Resume", "toggle")
        resumeBut.onChange { changeEvent, visTextButton -> isPaused = false }

        val quitBut = VisTextButton("Quit", "toggle")
        quitBut.onChange { changeEvent, visTextButton ->
            world.clear()

            manager.show("main")
        }

        pauseMenuControl = MenuControl(resumeBut, quitBut)
        pauseWindow.add(pauseMenuControl)
    }

    override fun show() {
        val factoryManager: FactoryManager = world.getSystem()
        val cameraTrackingSystem: CameraTrackingSystem = world.getSystem()
        val playerInputSystem: PlayerInputSystem = world.getSystem()

        val cameraFactory = cameraFactory.build(kodein, world)
        val playerFactory = playerFactory.build(kodein, world)
        //val platformFactory = PlatformFactory(kodein, world)
        val junkFactory = junkFactory.build(kodein, world)


            val cameraHandle = cameraFactory.create()
            display.cameraHandle = cameraHandle
            display.cameraSystem = world.getSystem()

            //val point = world.create()
            //transMapper.create(point).initPos(5f, 5f)
            //cameraTrackingSystem.addTarget(cameraHandle, point)

            for (i in 0 until worldSetup.players.size) {
                val playerInfo: PlayerInfo = worldSetup.players[i]

                val id = playerFactory.create(2f, 3f + i * 2)
                playerInputSystem.setInput(id, playerInfo.input)

                cameraTrackingSystem.addTarget(cameraHandle, id)
            }

            for (i in 0..1) {
                for (j in 0..25) {
                    junkFactory.create(11f + i * 2f, 1f + j)
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
            if (value) {
                root.addActor(pauseContainer)
            } else {
                pauseMenuControl.playerInput = null
                root.removeActor(pauseContainer)
            }
        }

    override fun update(delta: Float) {
        if (!isPaused) {
            updateManager.update(delta)

            for (playerInfo in worldSetup.players) {
                if (playerInfo.input.pollAction(Actions.ESCAPE)) {
                    pauseMenuControl.playerInput = playerInfo.input
                    isPaused = true
                }
            }
        }
    }

    override fun render(batch: SpriteBatch) {
        if (!isPaused) {
            renderManager.render(display, batch, updateManager.progress)
        }
    }

    override fun dispose() {
        display.dispose()
    }
}
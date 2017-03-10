package gabek.sm2.screens

import com.artemis.Aspect
import com.artemis.World
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.components.PlayerInputCom
import gabek.sm2.graphics.DisplayBuffer
import gabek.sm2.input.Actions
import gabek.sm2.leveltemplete.ActorTemplate
import gabek.sm2.leveltemplete.LevelTemplate
import gabek.sm2.systems.FactoryManager
import gabek.sm2.systems.LevelTemplateLoader
import gabek.sm2.systems.PlayerInputSystem
import gabek.sm2.systems.graphics.CameraTrackingSystem
import gabek.sm2.ui.MenuControl
import gabek.sm2.world.*
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class PlayingScreen(val kodein: Kodein) : Screen() {
    private val display = DisplayBuffer()

    private val world: World = kodein.instance()
    private val worldConfig: WorldConfig = kodein.instance()

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

        val levelTemplate = LevelTemplate()
        val worldConfig = kodein.instance<WorldConfig>()

        val json = JsonReader().parse(Gdx.files.getFileHandle("assets/level_templetes/primer.json", Files.FileType.Internal))
        world.getSystem(LevelTemplateLoader::class.java).loadLevel(json, worldConfig)

        val player = world.aspectSubscriptionManager.get(Aspect.all(PlayerInputCom::class.java)).entities[0]
        playerInputSystem.setInput(player, worldConfig.players[0].input)

        val cameraHandle = factoryManager.create("camera")
        cameraTrackingSystem.addTarget(cameraHandle, player)

        display.cameraHandle = cameraHandle
        display.cameraSystem = world.getSystem()

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

            for (playerInfo in worldConfig.players) {
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
package gabek.sm2.screens

import com.artemis.World
import com.artemis.managers.GroupManager
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.utils.JsonReader
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.components.common.TranslationCom
import gabek.sm2.graphics.Display
import gabek.sm2.input.Actions
import gabek.sm2.systems.LevelTemplateLoader
import gabek.sm2.systems.PlayerInputSystem
import gabek.sm2.systems.common.PrefabManager
import gabek.sm2.systems.common.TranslationSystem
import gabek.sm2.systems.gamemodes.GameModeManager
import gabek.sm2.systems.graphics.CameraTrackingSystem
import gabek.sm2.systems.graphics.ParallaxRenderSystem
import gabek.sm2.ui.MenuControl
import gabek.sm2.util.clear
import gabek.sm2.util.getSystem
import gabek.sm2.world.*
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class PlayingScreen(val kodein: Kodein) : Screen() {
    private val display = Display(kodein)

    private val world: World = kodein.instance()
    private val worldConfig: WorldConfig = kodein.instance()

    private val updateManager = UpdateManager(world, 60f)
    private val renderManager: RenderManager = kodein.instance()

    private val pauseWindow: VisWindow = VisWindow("Pause")
    private val pauseContainer = Container(pauseWindow)
    private val pauseMenuControl: MenuControl

    private val gameOverTable = VisTable()
    private val gameOverControl: MenuControl

    private val deathFadIn = 0f

    init {
        // pause menu
        pauseContainer.setFillParent(true)
        pauseWindow.isMovable = false
        val resumeBut = VisTextButton("Resume", "toggle")
        resumeBut.onChange { _, _ -> isPaused = false }

        val quitBut = VisTextButton("Quit", "toggle")
        quitBut.onChange { _, _ ->
            world.clear()
            manager.show("main")
        }

        pauseMenuControl = MenuControl(resumeBut, quitBut)
        pauseWindow.add(pauseMenuControl)

        val retryBut = VisTextButton("Retry", "toggle")
        retryBut.onChange { _, _ ->
            isGameOver = false
            world.clear()
            launchLevel()
        }

        val gameOverQuitBut = VisTextButton("Quit", "toggle")
        gameOverQuitBut.onChange { _, _ ->
            world.clear()
            manager.show("main")
        }

        gameOverControl = MenuControl(retryBut, gameOverQuitBut)
        gameOverTable.add(gameOverControl)
        gameOverTable.setFillParent(true)
    }

    override fun show() {
        launchLevel()

        val displayContainer = Container<Display>(display).fill()
        displayContainer.setFillParent(true)
        root.addActor(displayContainer)
    }

    private fun launchLevel() {
        val loader: LevelTemplateLoader = world.getSystem()
        val prefabManager: PrefabManager = world.getSystem()
        val groupManager: GroupManager = world.getSystem()
        val parallaxSystem: ParallaxRenderSystem = world.getSystem()
        val gameModeManager: GameModeManager = world.getSystem()
        val cameraTrackingSystem: CameraTrackingSystem = world.getSystem()
        val transSystem: TranslationSystem = world.getSystem()
        val playerInputSystem: PlayerInputSystem = world.getSystem()

        val worldConfig: WorldConfig = kodein.instance()

        val json = JsonReader().parse(Gdx.files.getFileHandle("assets/level_templates/primer.json", Files.FileType.Internal))
        loader.loadLevel(json, worldConfig)

        val cameraHandle = prefabManager.create("camera")

        val playerSpawns = groupManager.getEntities("spawn.player")
        val playerConfigs = worldConfig.players

        for (i in 0 until playerConfigs.size) {
            if (i >= playerSpawns.size()) {
                break
            }

            val player = prefabManager.create("player")
            val playerSpawnLocation = playerSpawns[i].getComponent(TranslationCom::class.java)

            transSystem.teleport(player, playerSpawnLocation.x, playerSpawnLocation.y, 0f)
            playerInputSystem.setInput(player, playerConfigs[i].input)
            cameraTrackingSystem.addTarget(cameraHandle, player)
        }


        //parallaxSystem.createParallax("backgrounds:layer1", 1.0f, 1f)
        //parallaxSystem.createParallax("backgrounds:layer2", 0.8f, 1f)
        //parallaxSystem.createParallax("backgrounds:layer3", 0.7f, 1f)
        //parallaxSystem.createParallax("backgrounds:layer4", 0.6f, 1f)
        //parallaxSystem.createParallax("backgrounds:layer5", 0.5f, 1f)

        gameModeManager.onGameOver {
            isGameOver = true
        }

        display.setHandle(cameraHandle, world.getSystem())
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

    private var isGameOver: Boolean = false
        set(value) {
            field = value
            if (value) {
                root.addActor(gameOverTable)
            } else {
                root.removeActor(gameOverTable)
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
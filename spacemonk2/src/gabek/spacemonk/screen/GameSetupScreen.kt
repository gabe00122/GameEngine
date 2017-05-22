package gabek.spacemonk.screen

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import gabek.engine.core.input.InputManager
import gabek.engine.core.input.Actions
import gabek.engine.core.input.PlayerInput
import gabek.engine.core.screen.Screen
import gabek.engine.core.ui.CurserControl
import gabek.engine.core.ui.MenuControl
import gabek.engine.core.world.PlayerInfo
import gabek.engine.core.world.WorldConfig
import ktx.actors.onChange

/**
 * @author Gabriel Keith
 */
class GameSetupScreen(val kodein: Kodein) : Screen() {
    private val inputManager: InputManager = kodein.instance()
    private val worldConfig: WorldConfig = kodein.instance()

    private val table = VisTable()
    private val tableContainer = Container(table)

    private val curserControler = CurserControl(kodein)

    private val playerJoinWidgets = Array(4, { i -> PlayerJoinWidget(i) })
    private val playerInputSet = mutableSetOf<PlayerInput>()
    private var joinIndex = 0
    private val joinPool = mutableListOf<PlayerJoinWidget>()

    init {
        tableContainer.fill()
        tableContainer.setFillParent(true)

        val avitarGrid = VisTable()
        for (y in 0 until 2) {
            for (x in 0 until 4) {
                avitarGrid.add(VisTextButton("")).prefSize(20f).pad(2f)
            }
            avitarGrid.row()
        }

        table.add(avitarGrid).colspan(4).expand().row()

        for (i in 0..3) {
            table.add(playerJoinWidgets[i]).prefSize(100f).pad(10f)
        }
        table.row()

        val startGameBut = VisTextButton("Start")
        startGameBut.onChange { _, _ -> startGame() }
        table.add(startGameBut).colspan(4)

        root.addActor(tableContainer)
    }

    override fun update(delta: Float) {
        val input = inputManager.pollAllInputs(Actions.SELECT)

        if (input != null && !playerInputSet.contains(input)) {
            join(input)
        }
    }

    private fun startGame() {
        worldConfig.players.clear()
        for (i in 0 until playerJoinWidgets.size) {
            val playerInput = playerJoinWidgets[i].playerInput

            if (playerInput != null) {
                worldConfig.players.add(PlayerInfo(i, playerInput))
            }
        }

        manager.show("playing")
    }

    private fun join(input: PlayerInput) {
        if (joinPool.isEmpty()) {
            if (joinIndex < playerJoinWidgets.size) {
                curserControler.join(input, joinIndex)
                playerJoinWidgets[joinIndex++].playerInput = input
                playerInputSet.add(input)
            }
        } else {
            joinPool.removeAt(0).playerInput = input
            playerInputSet.add(input)
        }
    }

    private inner class PlayerJoinWidget(val index: Int): VisTable() {
        var playerInput: PlayerInput? = null
            set(value) {
                field = value

                clear()
                if (value != null) {
                    add("Player ${index + 1}").row()

                    val control = MenuControl(40f, VisTextButton("Ready", "toggle"), VisTextButton("Leave", "toggle"))
                    control.playerInput = playerInput
                    add(control)
                } else {
                    add("Empty")
                }
            }

        init {
            background("window-bg")
            add("Empty")
        }
    }
}
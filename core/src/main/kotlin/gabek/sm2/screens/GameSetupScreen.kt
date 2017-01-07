package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import gabek.sm2.PlayerInfo
import gabek.sm2.WorldSetup
import gabek.sm2.input.Actions
import gabek.sm2.input.PlayerInput
import gabek.sm2.input.PlayerInputManager
import gabek.sm2.ui.CurserControl
import gabek.sm2.ui.MenuControl
import ktx.actors.onChange
import ktx.actors.onClick

/**
 * @author Gabriel Keith
 */
class GameSetupScreen(val kodein: Kodein) : Screen() {
    private val inputManager: PlayerInputManager = kodein.instance()
    private val worldSetup: WorldSetup = kodein.instance()

    private val table = VisTable()
    private val tableContainer = Container(table)

    private val curserControler = CurserControl(kodein)

    private val playerJoinWidgets = Array(6, { i -> PlayerJoinWidget(i) })
    private val playerInputSet = mutableSetOf<PlayerInput>()
    private var joinIndex = 0
    private val joinPool = mutableListOf<PlayerJoinWidget>()

    init {
        tableContainer.fill()
        tableContainer.setFillParent(true)

        for (i in 0..2) {
            table.add(playerJoinWidgets[i]).prefSize(200f).pad(10f)
        }
        table.row()

        for (i in 3..5) {
            table.add(playerJoinWidgets[i]).prefSize(200f).pad(10f)
        }
        table.row()

        val startGameBut = VisTextButton("Start")
        startGameBut.onChange { changeEvent, visTextButton -> startGame() }
        table.add(startGameBut).colspan(3)

        root.addActor(tableContainer)
        root.addActor(curserControler)
    }

    override fun update(delta: Float) {
        inputManager.update(delta)
        val input = inputManager.pollAllInputs(Actions.SELECT)

        if (input != null && !playerInputSet.contains(input)) {
            join(input)
        }
    }

    private fun startGame(){
        worldSetup.players.clear()
        for(i in 0 until playerJoinWidgets.size){
            val playerInput = playerJoinWidgets[i].playerInput

            if(playerInput != null) {
                worldSetup.players.add(PlayerInfo(i, playerInput))
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

    private inner class PlayerJoinWidget(val index: Int) : VisTable() {
        var playerInput: PlayerInput? = null
            set(value) {
                field = value

                clear()
                if(value != null) {
                    add("Player ${index + 1}").row()

                    val control = MenuControl(VisTextButton("Ready", "toggle"), VisTextButton("Leave", "toggle"))
                    //control.playerInput = playerInput
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
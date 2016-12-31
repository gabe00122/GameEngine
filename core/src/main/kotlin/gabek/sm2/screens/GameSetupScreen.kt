package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.PlayerInfo
import gabek.sm2.ui.MenuControl
import gabek.sm2.screens.Screen
import gabek.sm2.input.PlayerInput
import gabek.sm2.input.PlayerInputManager

/**
 * @author Gabriel Keith
 */
class GameSetupScreen(val kodein: Kodein) : Screen(){
    private val inputManager: PlayerInputManager = kodein.instance()

    private val table = VisTable()
    private val tableContainer = Container(table)

    private val playerJoinWidgets = Array(6, {i -> PlayerJoinWidget(i)})
    private val playerInputSet = mutableSetOf<PlayerInput>()
    private var joinIndex = 0
    private val joinPool = mutableListOf<PlayerJoinWidget>()

    init {
        tableContainer.fill()
        tableContainer.setFillParent(true)

        for(i in 0 .. 2){
            table.add(playerJoinWidgets[i]).prefSize(200f).pad(10f)
        }
        table.row()

        for(i in 3 .. 5){
            table.add(playerJoinWidgets[i]).prefSize(200f).pad(10f)
        }
    }

    override fun show() {
        root.addActor(tableContainer)
    }

    override fun update(delta: Float) {
        inputManager.update(delta)
        val input = inputManager.pollAllInputs(PlayerInput.Actions.SELECT)

        if(input != null && !playerInputSet.contains(input)) {
            join(input)
        }
    }

    private fun join(input: PlayerInput){
        if(joinPool.isEmpty()){
            if(joinIndex < playerJoinWidgets.size){
                playerJoinWidgets[joinIndex++].join(input)
                playerInputSet.add(input)
            }
        } else {
            joinPool.removeAt(0).join(input)
            playerInputSet.add(input)
        }
    }

    private inner class PlayerJoinWidget(val index: Int) : VisTable(){
        init{
            background("window-bg")
            add("Empty")
        }

        fun join(playerInput: PlayerInput){
            clear()
            add("Player ${index + 1}").row()

            val control = MenuControl(VisTextButton("Ready", "toggle"), VisTextButton("Leave", "toggle"))
            control.playerInput = playerInput
            add(control)
        }

        fun leave(){
            clear()
            add("Empty")
        }
    }
}
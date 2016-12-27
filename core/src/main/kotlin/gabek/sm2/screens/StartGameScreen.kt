package gabek.sm2.screens

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisWindow
import gabek.sm2.PlayerInfo
import gabek.sm2.Screen
import gabek.sm2.input.PlayerInput
import gabek.sm2.input.PlayerInputManager

/**
 * @author Gabriel Keith
 */
class StartGameScreen(val kodein: Kodein) : Screen(){
    private val inputManager: PlayerInputManager = kodein.instance()

    private val table = VisTable()
    private val tableContainer = Container(table)

    private val inputSet = mutableSetOf<PlayerInput>()
    private var index = 0

    init {
        tableContainer.fill()
        tableContainer.setFillParent(true)
    }

    override fun show() {
        root.addActor(tableContainer)
    }

    override fun update(delta: Float) {
        val input = inputManager.pollAllInputs(PlayerInput.Actions.SELECT)
        if(input != null && inputSet.add(input)){
            table.add(PlayerWidget(PlayerInfo(index++, input))).size(20f)
        }
    }

    private inner class PlayerWidget(playerInfo: PlayerInfo) : VisTable(){
        init{
            background("window")
            add("${playerInfo.index}")
        }
    }
}
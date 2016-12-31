package gabek.sm2.ui

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTable
import gabek.sm2.input.PlayerInput
import gabek.sm2.screens.buttonWidth
import gabek.sm2.screens.eagePad
import gabek.sm2.screens.menuPad

/**
 * @author Gabriel Keith
 */
class MenuControl : VisTable {
    private val defaltCooldown = 0.25f
    private var delay = 0f

    private var index = -1
    private var selected: Button? = null
    private val items = mutableListOf<Button>()

    var playerInput: PlayerInput? = null

    constructor(): super()

    constructor(vararg buttons: Button): super(){
        setItems(*buttons)
    }

    fun setItems(vararg buttons: Button) {
        for(i in 0 until buttons.size) {
            val button = buttons[i]

            button.setProgrammaticChangeEvents(false)
            items.add(button)

            val cell = add(button)
            cell.width(buttonWidth)
            cell.pad(eagePad)
            if (i != 0) {
                cell.padTop(menuPad)
            }
            if (i != buttons.size - 1) {
                cell.padBottom(menuPad)
                row()
            }
        }
    }

    fun down() {
        if (items.isNotEmpty()) {
            index++
            if (index >= items.size) {
                index = 0
            }
            setIndex(index)
        }
    }

    fun up() {
        if (items.isNotEmpty()) {
            index--
            if (index < 0) {
                index = items.size - 1
            }
            setIndex(index)
        }
    }

    fun select() {
        selected?.fire(ChangeListener.ChangeEvent())
    }

    override fun act(delta: Float) {
        super.act(delta)

        val input = playerInput
        if (input != null) {
            input.update(delta)

            if (!input.pollAction(PlayerInput.Actions.UP) && !input.pollAction(PlayerInput.Actions.DOWN)) {
                delay = 0f
            }

            if (delay <= 0) {
                if (input.pollAction(PlayerInput.Actions.UP)) {
                    up()
                    delay += defaltCooldown
                } else if (input.pollAction(PlayerInput.Actions.DOWN)) {
                    down()
                    delay += defaltCooldown
                } else if (input.pollAction(PlayerInput.Actions.SELECT)) {
                    select()
                }
            } else {
                delay -= delta
            }
        }
    }

    private fun setIndex(index: Int) {
        selected?.isChecked = false

        selected = items[index]
        items[index].isChecked = true
    }
}
package gabek.engine.core.ui

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.Focusable
import com.kotcrab.vis.ui.widget.VisTable
import gabek.engine.core.input.Actions
import gabek.engine.core.input.PlayerInput
import gabek.engine.core.screens.eagePad
import gabek.engine.core.screens.menuPad

/**
 * @author Gabriel Keith
 */
class MenuControl: VisTable {
    private val defaltCooldown = 0.25f
    private var selectPressed = false
    private var delay = 0f

    private var index = -1
    private var selected: Button? = null
    private val items = mutableListOf<Button>()

    private val buttonWidth: Float

    var playerInput: PlayerInput? = null

    constructor() : super() {
        buttonWidth = 80f
    }

    constructor(vararg buttons: Button) : super() {
        buttonWidth = 80f
        setItems(*buttons)
    }

    constructor(buttonWidth: Float, vararg buttons: Button) : super() {
        this.buttonWidth = buttonWidth
        setItems(*buttons)
    }

    fun setItems(vararg buttons: Button) {
        for (i in 0 until buttons.size) {
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
            clearFocus()
            index++
            if (index >= items.size) {
                index = 0
            }
            setIndex(index)
        }
    }

    fun up() {
        if (items.isNotEmpty()) {
            clearFocus()
            index--
            if (index < 0) {
                index = items.size - 1
            }
            setIndex(index)
        }
    }

    fun selectPress() {
        val selected = selected

        if (selected != null) {
            selectPressed = true
            selected.isChecked = true
        }
    }

    fun selectRelease() {
        val selected = selected
        if (selectPressed && selected != null) {
            selectPressed = false
            selected.fire(ChangeListener.ChangeEvent())
            selected.isChecked = false
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        val input = playerInput
        if (input != null) {
            if (!input.pollAction(Actions.UP) && !input.pollAction(Actions.DOWN)) {
                delay = 0f
            }

            if (delay <= 0) {
                if (input.pollAction(Actions.UP)) {
                    up()
                    delay += defaltCooldown
                }
                if (input.pollAction(Actions.DOWN)) {
                    down()
                    delay += defaltCooldown
                }
                if (input.pollAction(Actions.SELECT)) {
                    selectPress()
                } else if (selectPressed) {
                    selectRelease()
                }
            } else {
                delay -= delta
            }
        }
    }

    private fun clearFocus() {
        if (index >= 0 && index < items.size) {
            val btn = items[index]
            if (btn is Focusable) {
                btn.focusLost()
            }

            if (selectPressed) {
                selectPressed = false
                btn.isChecked = false
            }
        }
    }

    private fun setIndex(index: Int) {
        selected?.isChecked = false

        selected = items[index]
        val btn = items[index]
        if (btn is Focusable) {
            btn.focusGained()
        }
    }
}